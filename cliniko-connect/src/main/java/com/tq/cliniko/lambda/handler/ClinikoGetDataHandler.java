package com.tq.cliniko.lambda.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.exception.ClinikoConnectException;
import com.tq.cliniko.lambda.model.AppointmentType;
import com.tq.cliniko.lambda.model.Businesses;
import com.tq.cliniko.lambda.model.BusinessesInfo;
import com.tq.cliniko.lambda.model.ClinikoAppointmentType;
import com.tq.cliniko.lambda.model.ClinikoPractitionerConnectReq;
import com.tq.cliniko.lambda.model.Practitioner;
import com.tq.cliniko.lambda.model.PractitionersInfo;
import com.tq.cliniko.lambda.model.User;
import com.tq.cliniko.lambda.resp.ClinikoConnectStatusResponse;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.simplybook.exception.SbmSDKException;

public class ClinikoGetDataHandler implements ConnectHandler {
	private static final Logger m_log = LoggerFactory.getLogger(ClinikoGetDataHandler.class);
	private ClinikoSyncToSbmService clinikoSyncService = null;
	private ClinikoApiServiceBuilder apiServiceBuilder = null;

	public ClinikoGetDataHandler(ClinikoSyncToSbmService clinikoSyncService, ClinikoApiServiceBuilder apiServiceBuilder) {
		this.clinikoSyncService = clinikoSyncService;
		this.apiServiceBuilder = apiServiceBuilder;
	}

	@Override
	public ClinikoConnectStatusResponse handle(ClinikoPractitionerConnectReq req)
			throws SbmSDKException, ClinikoSDKExeption {
		m_log.info("Starting to get data from apiKey: "+ req.getParams().getApiKey());
		ClinikoConnectStatusResponse response = new ClinikoConnectStatusResponse();
		String apiKey = req.getParams().getApiKey();
		ClinikoSbmSync clinikoSbmSync = clinikoSyncService.queryWithIndex(apiKey);
		Map<Integer, Practitioner> mapPractitioners = new HashMap<Integer, Practitioner>();
		Map<Integer, List<Businesses>> mapBusinesses = new HashMap<Integer, List<Businesses>>();
		Map<Integer, List<AppointmentType>> mapAppointmentTypes = new HashMap<Integer, List<AppointmentType>>();
		if (clinikoSbmSync == null) {
			ClinikoAppointmentService clinikoService = apiServiceBuilder.build(apiKey);
			m_log.info("Checking apiKey: "+ req.getParams().getApiKey());
			User user = clinikoService.getAuthenticateUser();
			if (user == null) {
				m_log.info("The provided API Key is invalid");
				throw new ClinikoConnectException("The provided API Key is invalid");
			}
			m_log.info("The apiKey is valid. Able to get data from the apiKey");
			PractitionersInfo allPractitioners = clinikoService.getAllPractitioner();
			BusinessesInfo allBusinesses = clinikoService.getListBusinesses();
			Map<String, PractitionersInfo> allPractitionerOfBusinessCached = new HashMap<String, PractitionersInfo>();
			Map<Integer, ClinikoAppointmentType> allAppointmentTypesOfPractitionerCached = new HashMap<Integer, ClinikoAppointmentType>();
			for (Practitioner practitioner : allPractitioners.getPractitioners()) {
				mapPractitioners.put(practitioner.getId(), practitioner);
				for (Businesses business : allBusinesses.getBusinesses()) {
					PractitionersInfo allPractitionerOfBusiness = allPractitionerOfBusinessCached.get(business.getPractitioners().getLinks().getSelf());
					if (allPractitionerOfBusiness == null) {
						allPractitionerOfBusiness = clinikoService.getBusinessPractitioner(business.getPractitioners().getLinks().getSelf());
						allPractitionerOfBusinessCached.put(business.getPractitioners().getLinks().getSelf(), allPractitionerOfBusiness);
					}
					for (Practitioner practitionerOfBusiness : allPractitionerOfBusiness.getPractitioners()) {
						
						if (practitioner.getId().intValue() == practitionerOfBusiness.getId().intValue()) {
							List<Businesses> businessList = mapBusinesses.get(practitioner.getId());
							if (businessList == null) {
								businessList = new ArrayList<Businesses>();
								mapBusinesses.put(practitioner.getId(), businessList);
							}
							businessList.add(business);
							
							//appointment type
							ClinikoAppointmentType allAppointmentTypesOfPractitioner = allAppointmentTypesOfPractitionerCached.get(practitioner.getId());
							if (allAppointmentTypesOfPractitioner == null) {
								allAppointmentTypesOfPractitioner = clinikoService.getAppointmentType(practitioner.getId());
								allAppointmentTypesOfPractitionerCached.put(practitioner.getId(), allAppointmentTypesOfPractitioner);
							}
							
							for (AppointmentType appointmentType : allAppointmentTypesOfPractitioner.getAppointment_types()) {
								if (business.getAppointment_type_ids().contains(appointmentType.getId())) {
									List<AppointmentType> appointmentTypeList = mapAppointmentTypes.get(practitioner.getId());
									if (appointmentTypeList == null) {
										appointmentTypeList = new ArrayList<AppointmentType>();
										mapAppointmentTypes.put(practitioner.getId(), appointmentTypeList);
									}
									if(!appointmentTypeList.contains(appointmentType)) {
										appointmentTypeList.add(appointmentType);
									}
								}
							}
							break;
						}
					}
				}
			}

		} else {
			throw new ClinikoConnectException("This practitioner has already connected");
		}
		
		response.setSucceeded(true);
		response.setPractitionersGroupById(mapPractitioners);
		response.setBusinessesGroupByPractitionerId(mapBusinesses);
		response.setAppointmentTypesGroupByPractitioner(mapAppointmentTypes);
		m_log.info("All data is got fully");
		return response;
	}
}
