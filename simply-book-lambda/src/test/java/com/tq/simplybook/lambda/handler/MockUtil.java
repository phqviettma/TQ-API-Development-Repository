package com.tq.simplybook.lambda.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tq.simplybook.context.Env;

public class MockUtil {
	public static Env mockEnv() {
		Env env = mock(Env.class);
		when(env.getSimplyBookCompanyLogin()).thenReturn("canhcanh");
		when(env.getSimplyBookUser()).thenReturn("admin");
		when(env.getSimplyBookPassword()).thenReturn("");
		when(env.getSimplyBookSecretKey()).thenReturn("");
		when(env.getSimplyBookApiKey()).thenReturn("");
		when(env.getSimplyBookServiceUrlLogin()).thenReturn("https://user-api.simplybook.asia/login");
		when(env.getSimplyBookAdminServiceUrl()).thenReturn("https://user-api.simplybook.asia/admin/");
		when(env.getInfusionSoftApiName()).thenReturn("https://fd940.infusionsoft.com/api/xmlrpc");
		when(env.getInfusionSoftApiKey()).thenReturn("");
		when(env.getInfusionSoftAppointmentTimeField()).thenReturn("_AppointmentTime0");
		when(env.getInfusionSoftAppointmentLocationField()).thenReturn("_AppointmentLocation");
		when(env.getInfusionSoftServiceProviderField()).thenReturn("_ServiceProvider");
		when(env.getInfusionSoftAppointmentInstructionField()).thenReturn("_AppointmentInstructions");
		when(env.getInfusionSoftCreateAppliedTag()).thenReturn(639);
		when(env.getInfusionSoftCancelAppliedTag()).thenReturn(643);
		when(env.getClinikoApiKey()).thenReturn("");
		when(env.getCliniko_standard_appointment()).thenReturn(270071);
		when(env.getClinikoPatientId()).thenReturn(47508555);
		when(env.getCliniko_end_time()).thenReturn("18:00");
		when(env.getCliniko_start_time()).thenReturn("09:00");
		when(env.getInfusionftAppointmentDate()).thenReturn("_AppointmentDate2");
		when(env.getInfusionsoftPractitionerFirstName()).thenReturn("_PractitionerFirstName");
		when(env.getInfusionsoftPractitionerFirstName()).thenReturn("_PractitionerLastName");
		when(env.getGoogleClientId()).thenReturn("");
		when(env.getGoogleClientSecrets()).thenReturn("");
		return env;
	}
}
