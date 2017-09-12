package com.tq.cliniko.lambda.client;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.tq.cliniko.lambda.model.AppointmentInfo;

public class TestClinikoClient {

	public void testGet() throws Exception {

		
	/*	String response = ClinikoClient.request(
				new GetAppointmentsApiReq("b66b4caab8e4fd3e03b1dc88aa064339",null));
		System.out.println(response);
		response = ClinikoClient.request(new GetAppointmentsApiReq("https://api.cliniko.com/v1",
				"b66b4caab8e4fd3e03b1dc88aa064339", "appointment_start:>2019-09-11T03:30:00.000Z"));
		System.out.println(response);
*/
	}

	public void testCreatAppointment() throws Exception {
	
		AppointmentInfo appointmentInfo = new AppointmentInfo();
		appointmentInfo.setAppointment_start("2017-09-11T04:45:00.000Z");
		appointmentInfo.setPatient_id(44083214);
		appointmentInfo.setAppointment_type_id(232334);
		appointmentInfo.setBusiness_id(53724);
		appointmentInfo.setPractitioner_id(80819);

		String responseCreateAppointment = UtilsExecutor.request(new PostClinikoApiReq("https://api.cliniko.com/v1", "",appointmentInfo));
		System.out.println(responseCreateAppointment);

	}

	public void testGetAppointment() throws Exception {
		Env env = mock(Env.class);
		when(env.getApiKey()).thenReturn("");
		when(env.getApiUrl()).thenReturn("https://api.cliniko.com/v1");
		String response = UtilsExecutor.request(new GetAppointmentApiReq("", ""));
		System.out.println(response);

	}

	public void testDeleteAppointment() throws Exception {
		Env env = mock(Env.class);
		when(env.getApiKey()).thenReturn("");
		when(env.getApiUrl()).thenReturn("https://api.cliniko.com/v1");
		String response = UtilsExecutor.request(new DeleteAppointmentApiReq("https://api.cliniko.com/v1", "", ""));
		System.out.println(response);

	}

}
