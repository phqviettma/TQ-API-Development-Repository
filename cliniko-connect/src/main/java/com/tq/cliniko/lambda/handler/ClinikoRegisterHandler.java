package com.tq.cliniko.lambda.handler;

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
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikiAppointmentServiceImpl;
import com.tq.cliniko.lambda.exception.ClinikoConnectException;
import com.tq.cliniko.lambda.model.Businesses;
import com.tq.cliniko.lambda.model.BusinessesInfo;
import com.tq.cliniko.lambda.model.ClinikoPractitionerInfo;
import com.tq.cliniko.lambda.model.Practitioner;
import com.tq.cliniko.lambda.model.PractitionersInfo;
import com.tq.cliniko.lambda.model.User;
import com.tq.common.lambda.dynamodb.dao.ClinikoSyncToSbmDaoImpl;
import com.tq.common.lambda.dynamodb.impl.ClinikoSyncToSbmServiceImpl;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.impl.SbmUnitServiceImpl;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.service.TokenServiceSbm;
import com.tq.simplybook.service.SbmUnitService;

public class ClinikoRegisterHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static int STATUS_CODE = 200;
	private static final Logger m_log = LoggerFactory.getLogger(ClinikoRegisterHandler.class);
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private SbmUnitService unitServiceSbm = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private Env eVariables = null;
	private AmazonDynamoDB amazonDynamoDB = null;
	private ClinikoSyncToSbmService clinikoSyncService = null;

	public ClinikoRegisterHandler() {
		this.eVariables = Env.load();
		this.tokenServiceSbm = new TokenServiceImpl();
		this.amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(eVariables.getRegions(), eVariables.getAwsAccessKeyId(),
				eVariables.getAwsSecretAccessKey());
		;
		this.unitServiceSbm = new SbmUnitServiceImpl();
		this.clinikoSyncService = new ClinikoSyncToSbmServiceImpl(new ClinikoSyncToSbmDaoImpl(amazonDynamoDB));

	}

	ClinikoRegisterHandler(Env env, AmazonDynamoDB db, SbmUnitService unitService, TokenServiceSbm tokenService,
			ClinikoSyncToSbmService clinikoDbService) {
		this.amazonDynamoDB = db;
		this.unitServiceSbm = unitService;
		this.tokenServiceSbm = tokenService;
		this.clinikoSyncService = clinikoDbService;
		this.eVariables = env;
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		m_log.info("Received one request with body " + input.getBody());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("content-Type", "application/json");
		resp.setHeaders(headers);
		ClinikoPractitionerInfo info = getPractitionerInfo(input.getBody());
		try {
			if (info != null) {
				String companyLogin = eVariables.getSimplyBookCompanyLogin();
				String user = eVariables.getSimplyBookUser();
				String password = eVariables.getSimplyBookPassword();
				String loginEndPoint = eVariables.getSimplyBookServiceUrlLogin();
				String endpoint = eVariables.getSimplyBookAdminServiceUrl();
				String token = tokenServiceSbm.getUserToken(companyLogin, user, password, loginEndPoint);
				List<UnitProviderInfo> unitInfos = unitServiceSbm.getUnitList(companyLogin, endpoint, token, true, true,
						1);
				Map<String, String> clinikoMap = new HashMap<>();
				if ("connect".equals(info.getAction())) {
					ClinikoSbmSync clinikoSbmSync = clinikoSyncService.load(info.getApiKey());
					if (clinikoSbmSync == null) {
						ClinikiAppointmentServiceImpl clinikoService = new ClinikiAppointmentServiceImpl(
								info.getApiKey());
						BusinessesInfo businesses = clinikoService.getListBusinesses();
						User userInfo = clinikoService.getAuthenticateUser();
						String email = userInfo.getEmail();
						PractitionersInfo practitionerInfo = clinikoService.getPractitioner(userInfo.getId());

						for (Businesses business : businesses.getBusinesses()) {
							String link = business.getPractitioners().getLinks().getSelf();
							PractitionersInfo practitionersInfo = clinikoService.getBusinessPractitioner(link);
							for (Practitioner practitioners : practitionersInfo.getPractitioners()) {
								for (Practitioner practitioner : practitionerInfo.getPractitioners()) {
									if (practitioner.getId().equals(practitioners.getId())) {
										clinikoMap.put("clinikoId", business.getId() + "-" + practitioner.getId());
									}
								}
							}

						}

						for (UnitProviderInfo unitInfo : unitInfos) {
							if (unitInfo.getEmail() != null && unitInfo.getEmail().equals(email)) {
								if (unitInfo.getEvent_map() != null) {
									Set<String> eventInfos = unitInfo.getEvent_map().keySet();
									Iterator<String> it = eventInfos.iterator();
									if (it.hasNext()) {
										String eventId = it.next();
										String unitId = unitInfo.getId();
										String clinikoId = clinikoMap.get("clinikoId");
										clinikoSbmSync = new ClinikoSbmSync(info.getApiKey(), email, clinikoId,
												Integer.valueOf(unitId), Integer.valueOf(eventId));
										clinikoSyncService.put(clinikoSbmSync);

										m_log.info("Add to database successfully" + clinikoSbmSync);

									}
								}
							}
						}

					} else {
						throw new ClinikoConnectException("This practitioner has already connected");
					}
				} else if ("disconnect".equals(info.getAction())) {
					ClinikoSbmSync clinikoSbmSync = clinikoSyncService.load(info.getApiKey());
					if (clinikoSbmSync != null) {
						clinikoSyncService.delete(clinikoSbmSync);
						m_log.info("Disconnect successfully");
					} else {
						throw new ClinikoConnectException("This practitioner has not connected yet");
					}

				}

			}

		} catch (

		ClinikoConnectException e) {
			m_log.error("Error occurs", e);
			resp.setBody(buildErrorResponseBody(e.getMessage()));
			resp.setStatusCode(404);
			return resp;
		} catch (ClinikoSDKExeption e) {
			m_log.error("Error occurs", e);
			resp.setBody(buildErrorResponseBody());
			resp.setStatusCode(500);
			return resp;
		} catch (SbmSDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.setBody(buildSuccessResponseBody());
		resp.setStatusCode(STATUS_CODE);
		return resp;
	}

	public ClinikoPractitionerInfo getPractitionerInfo(String value) {
		ClinikoPractitionerInfo info = null;
		try {
			info = jsonMapper.readValue(value, ClinikoPractitionerInfo.class);
		} catch (Exception e) {
			m_log.error("Error during parsing {} : {} .", value, e);
		}
		return info;

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

	private static String buildSuccessResponseBody() {
		ObjectNode on = jsonMapper.createObjectNode();
		on.put("succeeded", true);
		return on.toString();
	}
}
