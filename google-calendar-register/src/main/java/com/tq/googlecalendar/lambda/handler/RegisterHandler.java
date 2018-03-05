package com.tq.googlecalendar.lambda.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tq.common.lambda.dynamodb.dao.ContactItemDaoImpl;
import com.tq.common.lambda.dynamodb.dao.GoogleCalendarDaoImpl;
import com.tq.common.lambda.dynamodb.impl.ContactItemServiceImpl;
import com.tq.common.lambda.dynamodb.impl.GoogleCalendarServiceImpl;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.exception.TrueQuitBadRequest;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceImpl;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.lambda.context.Env;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.UserInfoResp;
import com.tq.googlecalendar.req.StopWatchEventReq;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.req.WatchEventReq;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.resp.WatchEventResp;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.query.AddNewContactQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SbmUnitServiceImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.TokenServiceSbm;

public class RegisterHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static int STATUS_CODE = 200;
	private static String NO_TOKEN = "-BLANK-";
	private static final Logger m_log = LoggerFactory.getLogger(RegisterHandler.class);
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private SbmUnitService sbmUnitService = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private Env eVariables = null;
	private AmazonDynamoDB amazonDynamoDB = null;
	private GoogleCalendarDbService googleCalendarService = null;
	private ContactServiceInf contactService = null;
	private ContactItemService contactItemService = null;
	private TokenGoogleCalendarService tokenCalendarService = new TokenGoogleCalendarImpl();

	public RegisterHandler() {

		this.sbmUnitService = new SbmUnitServiceImpl();
		this.tokenServiceSbm = new TokenServiceImpl();
		this.eVariables = Env.load();
		this.amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(eVariables.getRegions(), eVariables.getAwsAccessKeyId(),
				eVariables.getAwsSecretAccessKey());
		;
		this.googleCalendarService = new GoogleCalendarServiceImpl(new GoogleCalendarDaoImpl(amazonDynamoDB));
		this.contactService = new ContactServiceImpl();
		this.tokenCalendarService = new TokenGoogleCalendarImpl();
		this.contactItemService = new ContactItemServiceImpl(new ContactItemDaoImpl(amazonDynamoDB));

	}

	// for testing only
	RegisterHandler(Env env, AmazonDynamoDB db, SbmUnitService unitService, TokenServiceSbm tokenService,
			GoogleCalendarDbService calendarService, ContactServiceInf contactService,
			ContactItemService contactItemService) {
		this.amazonDynamoDB = db;
		this.sbmUnitService = unitService;
		this.tokenServiceSbm = tokenService;
		this.googleCalendarService = calendarService;
		this.eVariables = env;
		this.contactService = contactService;
		this.contactItemService = contactItemService;
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		m_log.info("Received one request with body " + input.getBody());
		UserInfoResp info = getUserInfo(input.getBody());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("content-Type", "application/json");
		resp.setHeaders(headers);

		try {
			if (info != null) {
				String companyLogin = eVariables.getSimplyBookCompanyLogin();
				String user = eVariables.getSimplyBookUser();
				String password = eVariables.getSimplyBookPassword();
				String loginEndPoint = eVariables.getSimplyBookServiceUrlLogin();
				String endpoint = eVariables.getSimplyBookAdminServiceUrl();
				String googleEmail = info.getGoogleEmail();
				String sbmEmail = info.getEmail(); // the email was registered via clickfunnel affiliate
				if ("connect".equals(info.getAction())) {
					GoogleCalendarSbmSync googleCalendarSbmSync = googleCalendarService.query(sbmEmail);
					if (googleCalendarSbmSync == null) {
						ContactItem contactItem = contactItemService.load(sbmEmail);
						if (contactItem == null) {
							throw new TrueQuitRegisterException("The email " + sbmEmail + " is not signed up yet ");
						}
						TokenReq tokenReq = new TokenReq(eVariables.getGoogleClientId(),
								eVariables.getGoogleClientSecrets(), info.getRefreshToken());

						TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);
						String token = tokenServiceSbm.getUserToken(companyLogin, user, password, loginEndPoint);
						List<UnitProviderInfo> unitInfos = sbmUnitService.getUnitList(companyLogin, endpoint, token,
								true, true, 1);
						String apiName = eVariables.getInfusionSoftApiName();
						String apiKey = eVariables.getInfusionSoftApiKey();
						boolean done = false;
						for (UnitProviderInfo unitInfo : unitInfos) {
							if (unitInfo.getEmail() != null && unitInfo.getEmail().equals(sbmEmail)) {
								if (unitInfo.getEvent_map() != null) {
									Set<String> eventInfos = unitInfo.getEvent_map().keySet();
									Iterator<String> it = eventInfos.iterator();
									if (it.hasNext()) {
										String eventId = it.next();
										String unitId = unitInfo.getId();
										String sbmId = eventId + "-" + unitId;

										Map<String, String> dataRecord = new HashMap<>();
										dataRecord.put("Email", googleEmail);
										dataRecord.put("FirstName", info.getFirstName());
										dataRecord.put("LastName", info.getLastName());
										contactService.addWithDupCheck(apiName, apiKey,
												new AddNewContactQuery().withDataRecord(dataRecord));
										m_log.info("Add contact" + googleEmail + " email to infusionsoft successfully");
										GoogleCalendarApiServiceImpl googleApiService = new GoogleCalendarApiServiceImpl(
												tokenResp.getAccess_token());
										//Params params = new Params("3600000");
										WatchEventReq watchEventReq = new WatchEventReq(sbmId, "web_hook",
												eVariables.getGoogleNotifyDomain());
										WatchEventResp watchEventResp = googleApiService.watchEvent(watchEventReq,
												googleEmail);

										m_log.info("Watch calendar successfully with response: " + watchEventResp);

										GoogleCalendarSbmSync calendarSbm = new GoogleCalendarSbmSync(sbmId,
												googleEmail, info.getEmail(), info.getLastName(), info.getFirstName(),
												info.getAccessToken(), info.getRefreshToken(), NO_TOKEN,
												watchEventResp.getWatchResourceId());

										googleCalendarService.put(calendarSbm);
										m_log.info("Added to database successfully " + calendarSbm.toString());
										done = true;
										break;
									}
								}

							}

						}
						if (!done) {
							throw new TrueQuitRegisterException(
									"There is no Simplybook.me service provider associated to the provided e-mail "
											+ sbmEmail);
						}
					} else {
						throw new TrueQuitRegisterException("The email " + sbmEmail + " is already connected");
					}
				} else if ("disconnect".equals(info.getAction())) {
					GoogleCalendarSbmSync googleCalendarSbmSync = googleCalendarService.query(sbmEmail);
					if (googleCalendarSbmSync != null) {
						TokenReq tokenReq = new TokenReq(eVariables.getGoogleClientId(),
								eVariables.getGoogleClientSecrets(), googleCalendarSbmSync.getRefreshToken());

						TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);
						GoogleCalendarApiServiceImpl googleApiService = new GoogleCalendarApiServiceImpl(
								tokenResp.getAccess_token());
						StopWatchEventReq stopEventReq = new StopWatchEventReq(googleCalendarSbmSync.getSbmId(),
								googleCalendarSbmSync.getGcWatchResourceId());
						googleApiService.stopWatchEvent(stopEventReq);
						googleCalendarService.delete(googleCalendarSbmSync);
						m_log.info("Delete successfully");

					} else {
						throw new TrueQuitRegisterException("The email " + sbmEmail + " is not connected yet ");
					}
				}

			} else {
				throw new TrueQuitBadRequest("Bad request");
			}

		} catch (SbmSDKException e) {
			m_log.error("Error occurs", e);
			resp.setBody(buildErrorResponseBody());
			resp.setStatusCode(500);
			return resp;
		} catch (GoogleApiSDKException e) {
			m_log.error("Error occurs", e);
			resp.setBody(buildErrorResponseBody());
			resp.setStatusCode(500);
			return resp;
		} catch (InfSDKExecption e) {
			m_log.error("Error occurs", e);
			resp.setBody(buildErrorResponseBody());
			resp.setStatusCode(500);
		} catch (TrueQuitRegisterException e) {
			m_log.error("Error occurs", e);
			resp.setBody(buildErrorResponseBody(e.getMessage()));
			resp.setStatusCode(404);
			return resp;
		} catch (TrueQuitBadRequest e) {
			m_log.error("Error occurs", e);
			resp.setBody(buildErrorResponseBody());
			resp.setStatusCode(400);
			return resp;
		} catch (Exception e) {
			m_log.error("Error occurs", e);
			resp.setBody(buildErrorResponseBody());
			resp.setStatusCode(500);
			return resp;
		}

		resp.setBody(buildSuccessResponseBody());
		resp.setStatusCode(STATUS_CODE);
		return resp;
	}

	public UserInfoResp getUserInfo(String value) {
		UserInfoResp info = null;
		try {
			info = jsonMapper.readValue(value, UserInfoResp.class);
		} catch (IOException e) {
			m_log.error("Error during parsing {} : {} .", value, e);
		}
		return info;
	}

	private static String buildSuccessResponseBody() {
		ObjectNode on = jsonMapper.createObjectNode();
		on.put("succeeded", true);
		return on.toString();
	}

	private static String buildErrorResponseBody() {
		ObjectNode on = jsonMapper.createObjectNode();
		on.put("succeeded", false);
		return on.toString();
	}

	private static String buildErrorResponseBody(String errorMessage) {
		ObjectNode on = jsonMapper.createObjectNode();
		on.put("succeeded", false);
		on.put("error", errorMessage);
		return on.toString();
	}

}
