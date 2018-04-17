package com.tq.cliniko.impl;

import java.net.URLEncoder;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.AppointmentsInfo;
import com.tq.cliniko.lambda.model.BusinessesInfo;
import com.tq.cliniko.lambda.model.ClinikoAppointmentType;
import com.tq.cliniko.lambda.model.Patient;
import com.tq.cliniko.lambda.model.PatientPostReq;
import com.tq.cliniko.lambda.model.PractitionersInfo;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.lambda.model.User;
import com.tq.cliniko.lambda.req.ClinikoRespParser;
import com.tq.cliniko.lambda.req.DeleteClinikoApiReq;
import com.tq.cliniko.lambda.req.GetDirectUrlClinikoApiReq;
import com.tq.cliniko.lambda.req.PostClinikoApiReq;
import com.tq.cliniko.lambda.req.QueryClinikoApiReq;
import com.tq.cliniko.lambda.req.UtilsExecutor;
import com.tq.cliniko.service.ClinikoAppointmentService;

public class ClinikiAppointmentServiceImpl implements ClinikoAppointmentService {
	private final String m_clinikoApiKey;

	public ClinikiAppointmentServiceImpl(String clinikoApiKey) {
		this.m_clinikoApiKey = clinikoApiKey;
	}

	@Override
	public AppointmentsInfo getAppointments(String startTime, Integer maxResult, int practitionerId) throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor
					.request(new GetAppointmentsApiReq(m_clinikoApiKey, "appointment_start:>" + startTime, maxResult,practitionerId ));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, AppointmentsInfo.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}
	}

	@Override
	public BusinessesInfo getListBusinesses() throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor.request(new GetBusinessApiReq(m_clinikoApiKey));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, BusinessesInfo.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}

	}

	@Override
	public User getAuthenticateUser() throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor.request(new GetUserApiReq(m_clinikoApiKey));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, User.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}

	}

	@Override
	public AppointmentsInfo getDeletedAppointments(String startTime, Integer maxResultPerPage, int practitionerId)
			throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor.request(
					new GetDeletedAppointment(m_clinikoApiKey, "appointment_start:>" + startTime, maxResultPerPage,practitionerId));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, AppointmentsInfo.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}
	}

	@Override
	public AppointmentsInfo getCancelAppointments(String startTime, Integer maxResultPerPage, int practitionerId)
			throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor.request(
					new GetCancelAppointment(m_clinikoApiKey, "appointment_start:>" + startTime, maxResultPerPage,practitionerId));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, AppointmentsInfo.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}
	}

	@Override
	public PractitionersInfo getPractitioner(Integer userId) throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor.request(new GetPractitioner(m_clinikoApiKey, userId));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, PractitionersInfo.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}

	}

	@Override
	public AppointmentInfo getAppointment(Long id) throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor.request(new GetAppointmentApiReq(m_clinikoApiKey, id));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, AppointmentInfo.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}
	}

	@Override
	public AppointmentInfo createAppointment(AppointmentInfo appt) throws ClinikoSDKExeption {
		try {
			String jsonResp = UtilsExecutor.request(new PostAppointmentApiReq(m_clinikoApiKey, appt));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, AppointmentInfo.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}
	}

	@Override
	public boolean deleteAppointment(Long i) throws ClinikoSDKExeption {
		try {
			UtilsExecutor.request(new DeleteAppointmentApiReq(m_clinikoApiKey, i));
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}
		return true;
	}

	@Override
	public Settings getAllSettings() throws ClinikoSDKExeption {
		try {
			String jsonResp = UtilsExecutor.request(new GetAllSettingApiReq(m_clinikoApiKey));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, Settings.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}

	}

	@Override
	public AppointmentsInfo getAppointmentInfos() throws ClinikoSDKExeption {
		try {
			String jsonResp = UtilsExecutor.request(new GetAppointmentInfo(m_clinikoApiKey));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, AppointmentsInfo.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}

	}

	@Override
	public PractitionersInfo getBusinessPractitioner(String url) throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor.request(new GetPractitionerByUrl(m_clinikoApiKey, url));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, PractitionersInfo.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}

	}

	private class GetAppointmentsApiReq extends QueryClinikoApiReq {
		public GetAppointmentsApiReq(String apiKey, String queryStatement, int perPage,int practitionerId) throws Exception {
			super(apiKey, "appointments",
					"?q[]=" + "practitioner_id:="+practitionerId+"&per_page=" + String.valueOf(perPage)+"&"+ URLEncoder.encode(queryStatement, "UTF-8"));
		}
	}

	private class GetPractitioner extends QueryClinikoApiReq {

		public GetPractitioner(String apiKey, Integer userId) {
			super(apiKey, "practitioners", "?q=user_id:=" + userId);

		}

	}

	private class GetBusinessApiReq extends QueryClinikoApiReq {

		public GetBusinessApiReq(String apiKey) {
			super(apiKey, "businesses", null);

		}

	}

	private class GetUserApiReq extends QueryClinikoApiReq {

		public GetUserApiReq(String apiKey) {
			super(apiKey, "user", null);

		}

	}

	private class GetDeletedAppointment extends QueryClinikoApiReq {
		public GetDeletedAppointment(String apiKey, String queryStatement, int perPage,int practitionerId) throws Exception {
			super(apiKey, "appointments/deleted",
					"?q[]=" + "practitioner_id:="+practitionerId+"&per_page=" + String.valueOf(perPage)+"&"+ URLEncoder.encode(queryStatement, "UTF-8"));
		}
	}

	private class GetCancelAppointment extends QueryClinikoApiReq {
		public GetCancelAppointment(String apiKey, String queryStatement, int perPage,int practitionerId) throws Exception {
			super(apiKey, "appointments/cancelled",
					"?q[]="+ "practitioner_id:="+practitionerId+"&per_page=" + String.valueOf(perPage) +"&"+ URLEncoder.encode(queryStatement, "UTF-8") );
		}
	}

	private class GetAppointmentInfo extends QueryClinikoApiReq {

		public GetAppointmentInfo(String apiKey) {
			super(apiKey, "appointments", null);

		}

	}

	private class GetAppointmentApiReq extends QueryClinikoApiReq {
		public GetAppointmentApiReq(String apiKey, Long id) {
			super(apiKey, "appointments" + "/" + id, null);
		}
	}

	private class PostAppointmentApiReq extends PostClinikoApiReq {
		public PostAppointmentApiReq(String apiKey, Object object) {
			super(apiKey, "appointments", object);
		}
	}
	private class PostPatientApiReq extends PostClinikoApiReq{

		public PostPatientApiReq(String apiKey, Object object) {
			super(apiKey, "patients", object);
	
		}
		
	}
	private class DeleteAppointmentApiReq extends DeleteClinikoApiReq {
		public DeleteAppointmentApiReq(String apiKey, Long id) {
			super(apiKey, "appointments/" + id);
		}

	}

	private class GetAllSettingApiReq extends QueryClinikoApiReq {

		public GetAllSettingApiReq(String apiKey) {
			super(apiKey, "settings/public", null);

		}

	}

	@Override
	public AppointmentsInfo next(AppointmentsInfo apptInfo) throws ClinikoSDKExeption {
		String url = apptInfo.getLinks().getNext();
		return getAppointmentsByUrl(url);
	}

	private AppointmentsInfo getAppointmentsByUrl(String url) throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor.request(new GetDirectUrlClinikoApiReq(m_clinikoApiKey, url));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, AppointmentsInfo.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}

	}

	private class GetPractitionerByUrl extends GetDirectUrlClinikoApiReq {

		public GetPractitionerByUrl(String apiKey, String url) {
			super(apiKey, url);

		}

	}

	@Override
	public AppointmentsInfo getNewestAppointment(String latestTime, Integer maxResultPerPage, int practitionerId)
			throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor
					.request(new GetAppointmentsApiReq(m_clinikoApiKey, "updated_at:>" + latestTime, maxResultPerPage, practitionerId));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, AppointmentsInfo.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}
	}

	@Override
	public AppointmentsInfo getNewestDeletedAppointments(String startTime, Integer maxResultPerPage,int practitionerId)
			throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor
					.request(new GetDeletedAppointment(m_clinikoApiKey, "updated_at:>" + startTime, maxResultPerPage, practitionerId));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, AppointmentsInfo.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}
	}

	@Override
	public AppointmentsInfo getNewestCancelledAppointments(String startTime, Integer maxResultPerPage, int practitionerId)
			throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor
					.request(new GetCancelAppointment(m_clinikoApiKey, "updated_at:>" + startTime,maxResultPerPage,practitionerId ));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, AppointmentsInfo.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}
	}

	@Override
	public AppointmentsInfo getPractitionerAppointment(Integer practitionerId, Integer maxResultPerPage) throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor.request(new GetPractitionerAppointment(m_clinikoApiKey,
					"practitioner_id:=" + practitionerId, maxResultPerPage));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, AppointmentsInfo.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}
	}

	private class GetPractitionerAppointment extends QueryClinikoApiReq {
		public GetPractitionerAppointment(String apiKey, String queryStatement, int perPage) throws Exception {
			super(apiKey, "appointments",
					"?q[]=" + queryStatement + "&per_page=" + String.valueOf(perPage));
		}
	}

	@Override
	public Patient createPatient(String firstName, String lastName) throws ClinikoSDKExeption {
		try {
			String jsonResp = UtilsExecutor.request(new PostPatientApiReq(m_clinikoApiKey, new PatientPostReq(firstName, lastName)));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, Patient.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}
	}

	@Override
	public ClinikoAppointmentType getAppointmentType(Integer practitionerId) throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor
					.request(new GetAppointmentType(m_clinikoApiKey,practitionerId ));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, ClinikoAppointmentType.class);
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}
	}
	private class GetAppointmentType extends QueryClinikoApiReq{

		public GetAppointmentType(String apiKey,Integer practitionerId) {
			super(apiKey, "practitioners", "/"+practitionerId+"/appointment_types");
			
		}
		
	}
}
