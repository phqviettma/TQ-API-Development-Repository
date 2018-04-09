package com.tq.cliniko.lambda.handler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikiAppointmentServiceImpl;
import com.tq.cliniko.lambda.exception.ClinikoConnectException;
import com.tq.cliniko.lambda.model.Businesses;
import com.tq.cliniko.lambda.model.BusinessesInfo;
import com.tq.cliniko.lambda.model.ClinikoPractitionerConnectReq;
import com.tq.cliniko.lambda.model.Practitioner;
import com.tq.cliniko.lambda.model.PractitionersInfo;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.lambda.model.User;
import com.tq.cliniko.lambda.resp.ClinikoConnectStatusResponse;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppointment;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.LatestClinikoAppointmentService;
import com.tq.common.lambda.utils.TimeUtils;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.TokenServiceSbm;

public class ClinikoConnectHandler implements ConnectHandler {
	private Env eVariables = null;
	private static final Logger m_log = LoggerFactory.getLogger(ClinikoConnectHandler.class);
	private SbmUnitService unitServiceSbm = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private ClinikoSyncToSbmService clinikoSyncService = null;
	private LatestClinikoAppointmentService latestClinikoApptService = null;

	public ClinikoConnectHandler(Env env, SbmUnitService unitService, TokenServiceSbm tokenService,
			ClinikoSyncToSbmService clinikoSyncService, LatestClinikoAppointmentService latestClinikoApptService) {
		this.eVariables = env;
		this.unitServiceSbm = unitService;
		this.tokenServiceSbm = tokenService;
		this.clinikoSyncService = clinikoSyncService;
		this.latestClinikoApptService = latestClinikoApptService;
	}

	@Override
	public ClinikoConnectStatusResponse handle(ClinikoPractitionerConnectReq req)
			throws SbmSDKException, ClinikoSDKExeption {
		ClinikoConnectStatusResponse response = new ClinikoConnectStatusResponse();
		String companyLogin = eVariables.getSimplyBookCompanyLogin();
		String user = eVariables.getSimplyBookUser();
		String password = eVariables.getSimplyBookPassword();
		String loginEndPoint = eVariables.getSimplyBookServiceUrlLogin();
		String endpoint = eVariables.getSimplyBookAdminServiceUrl();
		String token = tokenServiceSbm.getUserToken(companyLogin, user, password, loginEndPoint);
		List<UnitProviderInfo> unitInfos = unitServiceSbm.getUnitList(companyLogin, endpoint, token, true, true, 1);
		Map<String, String> clinikoMap = new HashMap<>();
		String apiKey = req.getParams().getApiKey();
		ClinikoSbmSync clinikoSbmSync = clinikoSyncService.load(apiKey);
		if (clinikoSbmSync == null) {
			ClinikiAppointmentServiceImpl clinikoService = new ClinikiAppointmentServiceImpl(apiKey);
			BusinessesInfo businesses = clinikoService.getListBusinesses();
			User userInfo = clinikoService.getAuthenticateUser();
			String email = userInfo.getEmail();
			PractitionersInfo practitionerInfo = clinikoService.getPractitioner(userInfo.getId());
			boolean done = false;
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
							clinikoSbmSync = new ClinikoSbmSync(apiKey, email, clinikoId, Integer.valueOf(unitId),
									Integer.valueOf(eventId));
							clinikoSyncService.put(clinikoSbmSync);

							Settings settings = clinikoService.getAllSettings();
							String country = settings.getAccount().getCountry();
							String time_zone = settings.getAccount().getTime_zone();
							String latestUpdateTime = TimeUtils.getNowInUTC(country + "/" + time_zone);
							long timeStamp = Calendar.getInstance().getTimeInMillis();
							LatestClinikoAppointment latestClinikoAppointment = new LatestClinikoAppointment(apiKey,
									latestUpdateTime, 1, timeStamp);
							latestClinikoApptService.put(latestClinikoAppointment);
							m_log.info("Add to database successfully" + clinikoSbmSync);
							done = true;
							break;
						}
					}
				}
			}
			if (!done) {
				throw new ClinikoConnectException(
						"There is no Cliniko practitioner associated to the provided apiKey " + apiKey);
			}

		} else {
			throw new ClinikoConnectException("This practitioner has already connected");
		}
		response.setSucceeded(true);
		return response;
	}

}
