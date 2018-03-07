package com.tq.googlecalendar.lambda.handler;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceImpl;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.lambda.context.Env;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.lambda.model.GoogleConnectStatusResponse;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.req.Params;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.req.WatchEventReq;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.resp.WatchEventResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.TokenServiceSbm;

public class GoogleConnectCalendarHandler implements Handler {
	private static final Logger m_log = LoggerFactory.getLogger(GoogleConnectCalendarHandler.class);
	private static String NO_TOKEN = "-BLANK-";
	private static final String PARAMS = "3600000";
	private SbmUnitService sbmUnitService = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private Env eVariables = null;
	private GoogleCalendarDbService googleCalendarService = null;
	private ContactServiceInf contactService = null;
	private ContactItemService contactItemService = null;
	private GoogleCalendarApiServiceBuilder apiServiceBuilder = null;
	private TokenGoogleCalendarService tokenCalendarService = new TokenGoogleCalendarImpl();

	public GoogleConnectCalendarHandler(Env eVariables, GoogleCalendarDbService googleCalendarService,
			ContactServiceInf contactService, ContactItemService contactItemService,
			TokenGoogleCalendarService tokenCalendarService, SbmUnitService sbmUnitService,
			TokenServiceSbm tokenServiceSbm) {
		this.eVariables = eVariables;
		this.googleCalendarService = googleCalendarService;
		this.contactItemService = contactItemService;
		this.sbmUnitService = sbmUnitService;
		this.tokenCalendarService = tokenCalendarService;
		this.contactService = contactService;
		this.tokenServiceSbm = tokenServiceSbm;

	}
	public GoogleConnectCalendarHandler(Env eVariables, GoogleCalendarDbService googleCalendarService,
			ContactServiceInf contactService, ContactItemService contactItemService,
			TokenGoogleCalendarService tokenCalendarService, SbmUnitService sbmUnitService,
			TokenServiceSbm tokenServiceSbm,GoogleCalendarApiServiceBuilder apiServiceBuilder) {
		this.eVariables = eVariables;
		this.googleCalendarService = googleCalendarService;
		this.contactItemService = contactItemService;
		this.sbmUnitService = sbmUnitService;
		this.tokenCalendarService = tokenCalendarService;
		this.contactService = contactService;
		this.tokenServiceSbm = tokenServiceSbm;
		this.apiServiceBuilder = apiServiceBuilder;

	}

	@Override
	public GoogleConnectStatusResponse handle(GoogleRegisterReq req)
			throws GoogleApiSDKException, SbmSDKException, InfSDKExecption, TrueQuitRegisterException {
		GoogleConnectStatusResponse response = new GoogleConnectStatusResponse();
		String companyLogin = eVariables.getSimplyBookCompanyLogin();
		String user = eVariables.getSimplyBookUser();
		String password = eVariables.getSimplyBookPassword();
		String loginEndPoint = eVariables.getSimplyBookServiceUrlLogin();
		String endpoint = eVariables.getSimplyBookAdminServiceUrl();
		String googleEmail = req.getParams().getGoogleEmail();
		String sbmEmail = req.getParams().getEmail();
		GoogleCalendarSbmSync googleCalendarSbmSync = googleCalendarService.query(sbmEmail);
		if (googleCalendarSbmSync == null) {
			ContactItem contactItem = contactItemService.load(sbmEmail);
			if (contactItem == null) {
				throw new TrueQuitRegisterException("The email " + sbmEmail + " is not signed up yet ");
			}
			TokenReq tokenReq = new TokenReq(eVariables.getGoogleClientId(), eVariables.getGoogleClientSecrets(),
					req.getParams().getRefreshToken());

			TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);
			String token = tokenServiceSbm.getUserToken(companyLogin, user, password, loginEndPoint);
			List<UnitProviderInfo> unitInfos = sbmUnitService.getUnitList(companyLogin, endpoint, token, true, true, 1);
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
					
							/* Map<String, String> dataRecord = new HashMap<>();
							dataRecord.put("Email", googleEmail);
							dataRecord.put("FirstName", req.getParams().getFirstName());
							dataRecord.put("LastName", req.getParams().getLastName());
							contactService.addWithDupCheck(apiName, apiKey,
									new AddNewContactQuery().withDataRecord(dataRecord));
							m_log.info("Add contact" + googleEmail + " email to infusionsoft successfully"); */
							GoogleCalendarApiService googleApiService = apiServiceBuilder.build(tokenResp.getAccess_token());
							 Params params = new Params(PARAMS);
							WatchEventReq watchEventReq = new WatchEventReq(sbmId, "web_hook",
									eVariables.getGoogleNotifyDomain(), params);
							WatchEventResp watchEventResp = googleApiService.watchEvent(watchEventReq, googleEmail);

							m_log.info("Watch calendar successfully with response: " + watchEventResp);

							GoogleCalendarSbmSync calendarSbm = new GoogleCalendarSbmSync(sbmId, googleEmail,
									req.getParams().getEmail(), req.getParams().getLastName(),
									req.getParams().getFirstName(), req.getParams().getAccessToken(),
									req.getParams().getRefreshToken(), NO_TOKEN, watchEventResp.getWatchResourceId());

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
						"There is no Simplybook.me service provider associated to the provided e-mail " + sbmEmail);
			}
		} else {
			throw new TrueQuitRegisterException("The email " + sbmEmail + " is already connected");
		}
		response.setSucceeded(true);
		return response;

	}

}
