package com.tq.cliniko.lambda.client;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.tq.cliniko.lambda.model.AppointmentInfo;

public class TestClinikoClient {

	public void testGet() throws Exception {
		Env env = mock(Env.class);
		when(env.getApiKey()).thenReturn("");
		when(env.getApiUrl()).thenReturn("https://api.cliniko.com/v1");
		String response = ClinikoClient.request(new GetAppointmentsApiReq(env, null));
		System.out.println(response);
		response = ClinikoClient.request(new GetAppointmentsApiReq(env, "appointment_start:>2019-09-11T03:30:00.000Z"));
		System.out.println(response);

	}

	public void testCreatAppointment() throws Exception {
		Env env = mock(Env.class);
		when(env.getApiKey()).thenReturn("");
		when(env.getApiUrl()).thenReturn("https://api.cliniko.com/v1");
		AppointmentInfo appointmentInfo = new AppointmentInfo();
		appointmentInfo.setAppointment_start("2017-09-11T04:45:00.000Z");
		appointmentInfo.setPatient_id(44083214);
		appointmentInfo.setAppointment_type_id(232334);
		appointmentInfo.setBusiness_id(53724);
		appointmentInfo.setPractitioner_id(80819);

		String responseCreateAppointment = ClinikoClient.request(new CreateAppointmentApiReq(env, appointmentInfo));
		System.out.println(responseCreateAppointment);

	}

	public void testGetAppointment() throws Exception {
		Env env = mock(Env.class);
		when(env.getApiKey()).thenReturn("");
		when(env.getApiUrl()).thenReturn("https://api.cliniko.com/v1");
		String response = ClinikoClient.request(new GetAppointmentApiReq(env, "82978145"));
		System.out.println(response);

	}

	public void testDeleteAppointment() throws Exception {
		Env env = mock(Env.class);
		when(env.getApiKey()).thenReturn("");
		when(env.getApiUrl()).thenReturn("https://api.cliniko.com/v1");
		String response = ClinikoClient.request(new DeleteAppointmentApiReq(env, "appointments/82978145"));
		System.out.println(response);

	}

}
