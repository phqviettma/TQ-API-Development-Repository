package com.tq.cliniko.impl;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.AppointmentsInfo;
import com.tq.cliniko.lambda.req.ClinikoRespParser;
import com.tq.cliniko.lambda.req.DeleteClinikoApiReq;
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
	public AppointmentsInfo getAppointments(String startTime) throws ClinikoSDKExeption {
		String jsonResp;
		try {
			jsonResp = UtilsExecutor.request(new GetAppointmentsApiReq(m_clinikoApiKey, "appointment_start:>" + startTime));
			return ClinikoRespParser.readJsonValueForObject(jsonResp, AppointmentsInfo.class);
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
	public boolean deleteAppointment(Long id) throws ClinikoSDKExeption {
		try {
			UtilsExecutor.request(new DeleteAppointmentApiReq(m_clinikoApiKey, id));
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e);
		}
		return true;
	}
	
	private class GetAppointmentsApiReq extends QueryClinikoApiReq {
		public GetAppointmentsApiReq(String apiKey, String queryStatement) {
			super(apiKey, "appointments", queryStatement);
		}
	}
	
	private class GetAppointmentApiReq extends QueryClinikoApiReq{
		public GetAppointmentApiReq(String apiKey, Long id) {
			super(apiKey, "appointments" + "/" + id, null);
		}
	}
	
	private class PostAppointmentApiReq extends PostClinikoApiReq {
		public PostAppointmentApiReq(String apiKey, Object object) {
			super(apiKey, "appointments", object);
		}
	}
	
	private class DeleteAppointmentApiReq extends DeleteClinikoApiReq {
		public DeleteAppointmentApiReq(String apiKey, Long id) {
			super(apiKey, "appointments/" + id);
		}
		
	}

	


}
