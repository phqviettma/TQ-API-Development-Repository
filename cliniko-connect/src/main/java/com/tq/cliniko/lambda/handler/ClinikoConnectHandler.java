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
import com.tq.cliniko.lambda.model.AppointmentType;
import com.tq.cliniko.lambda.model.Businesses;
import com.tq.cliniko.lambda.model.BusinessesInfo;
import com.tq.cliniko.lambda.model.ClinikoAppointmentType;
import com.tq.cliniko.lambda.model.ClinikoPractitionerConnectReq;
import com.tq.cliniko.lambda.model.Patient;
import com.tq.cliniko.lambda.model.Practitioner;
import com.tq.cliniko.lambda.model.PractitionersInfo;
import com.tq.cliniko.lambda.model.User;
import com.tq.cliniko.lambda.resp.ClinikoConnectStatusResponse;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoItemService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
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
	private ClinikoItemService clinikoItemService = null;
	private ClinikoCompanyInfoService clinikoCompanyService = null;
	private static final String DEFAULT_PATIENT_NAME = "TrueQuit";
	private static final String DEFAULT_PATIENT_LAST_NAME = "patient";

	public ClinikoConnectHandler(Env env, SbmUnitService unitService, TokenServiceSbm tokenService,
			ClinikoSyncToSbmService clinikoSyncService, ClinikoItemService clinikoItemService,
			ClinikoCompanyInfoService clinikoCompanyService) {
		this.eVariables = env;
		this.unitServiceSbm = unitService;
		this.tokenServiceSbm = tokenService;
		this.clinikoSyncService = clinikoSyncService;
		this.clinikoItemService = clinikoItemService;
		this.clinikoCompanyService = clinikoCompanyService;
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
		ClinikoSbmSync clinikoSbmSync = clinikoSyncService.queryWithIndex(apiKey);
		if (clinikoSbmSync == null) {
			ClinikiAppointmentServiceImpl clinikoService = new ClinikiAppointmentServiceImpl(apiKey);
			User userInfo = clinikoService.getAuthenticateUser();
			String email = userInfo.getEmail();
			PractitionersInfo practitionerInfo = clinikoService.getPractitioner(userInfo.getId());
			boolean done = false;
			if (!practitionerInfo.getPractitioners().isEmpty()) {
				BusinessesInfo businesses = clinikoService.getListBusinesses();
				for (Businesses business : businesses.getBusinesses()) {
					String link = business.getPractitioners().getLinks().getSelf();
					PractitionersInfo practitionersInfo = clinikoService.getBusinessPractitioner(link);

					for (Practitioner practitioners : practitionersInfo.getPractitioners()) {
						for (Practitioner practitioner : practitionerInfo.getPractitioners()) {
							if (practitioner.getId().equals(practitioners.getId())) {
								clinikoMap.put("clinikoId", business.getId() + "-" + practitioner.getId());
								m_log.info("Business" + business.getId() + "Practitioner Id" + practitioner.getId());
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
								String sbmId = eventId + "-" + unitId;
								String clinikoId = clinikoMap.get("clinikoId");
								m_log.info("Cliniko Map" + clinikoMap);
								clinikoSbmSync = new ClinikoSbmSync(apiKey, email, clinikoId, sbmId);
								clinikoSyncService.put(clinikoSbmSync);
								m_log.info("Save to database successfully with value " + clinikoSbmSync);
								long timeStamp = Calendar.getInstance().getTimeInMillis();
								ClinikoSyncStatus clinikoItem = new ClinikoSyncStatus();
								clinikoItem.setApiKey(apiKey);
								clinikoItem.setTimeStamp(timeStamp);
								clinikoItemService.put(clinikoItem);
								m_log.info("Add to database successfully" + clinikoSbmSync);
								ClinikoCompanyInfo clinikoCompanyInfo = createDefaultPatient(clinikoId, apiKey);
								clinikoCompanyService.put(clinikoCompanyInfo);
								m_log.info("Save to database " + clinikoCompanyInfo);
								done = true;
								break;
							}
						}
					}
				}
			} else {
				throw new ClinikoConnectException("Do not have this practitioner in this business");
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

	private ClinikoCompanyInfo createDefaultPatient(String clinikoId, String apiKey) throws ClinikoSDKExeption {
		String clinikoCompanyId[] = clinikoId.split("-");
		Integer businessId = Integer.parseInt(clinikoCompanyId[0]);
		Integer practitionerId = Integer.parseInt(clinikoCompanyId[1]);
		ClinikoCompanyInfo clinikoCompanyInfo = clinikoCompanyService.load(businessId);
		if (clinikoCompanyInfo == null) {
			ClinikiAppointmentServiceImpl clinikoApiService = new ClinikiAppointmentServiceImpl(apiKey);
			ClinikoAppointmentType appointmentType = clinikoApiService.getAppointmentType(practitionerId);
			Integer apptTypeId = 0;
			for (AppointmentType apptType : appointmentType.getAppointment_types()) {
				apptTypeId = apptType.getId();
			}
			Patient patient = clinikoApiService.createPatient(DEFAULT_PATIENT_NAME, DEFAULT_PATIENT_LAST_NAME);
			clinikoCompanyInfo = new ClinikoCompanyInfo(patient.getId(), businessId, apptTypeId);
		}
		return clinikoCompanyInfo;
	}
}
