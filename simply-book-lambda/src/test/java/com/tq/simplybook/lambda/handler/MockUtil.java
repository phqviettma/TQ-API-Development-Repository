package com.tq.simplybook.lambda.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tq.simplybook.context.Env;

public class MockUtil {
	public static Env mockEnv() {
		Env env = mock(Env.class);
		when(env.getSimplyBookCompanyLogin()).thenReturn("truequit");
		when(env.getSimplyBookUser()).thenReturn("admin");
		when(env.getSimplyBookPassword()).thenReturn("");
		when(env.getSimplyBookSecretKey()).thenReturn("");
		when(env.getSimplyBookApiKey()).thenReturn("");
		when(env.getSimplyBookServiceUrlLogin()).thenReturn("https://user-api.simplybook.asia/login");
		when(env.getSimplyBookAdminServiceUrl()).thenReturn("https://user-api.simplybook.asia/admin/");
		when(env.getInfusionSoftApiName()).thenReturn("https://fd940.infusionsoft.com/api/xmlrpc");
		when(env.getInfusionSoftApiKey()).thenReturn("");
		when(env.getInfusionSoftAppointmentTimeField()).thenReturn("_AppointmentTime1");
		when(env.getInfusionSoftAppointmentLocationField()).thenReturn("_AppointmentLocation");
		when(env.getInfusionSoftServiceProviderField()).thenReturn("_ServiceProvider");
		when(env.getInfusionSoftAppointmentInstructionField()).thenReturn("_AppointmentInstructions");
		when(env.getInfusionSoftCreateAppliedTag()).thenReturn(639);
		when(env.getInfusionSoftCancelAppliedTag()).thenReturn(643);
		when(env.getInfusionftAppointmentDate()).thenReturn("_appointmentdate20");
		when(env.getInfusionsoftPractitionerFirstName()).thenReturn("_PractitionerFirstName");
		when(env.getInfusionsoftPractitionerLastName()).thenReturn("_PractitionerLastName");
		when(env.getInfusionsoftApptAddress1()).thenReturn("_AppointmentAddress1");
		when(env.getInfusionsoftApptAddress2()).thenReturn("_AppointmentAddress2");
		when(env.getInfusionsoftApptCity()).thenReturn("_AppointmentCity");
		when(env.getInfusionsoftApptCountry()).thenReturn("_AppointmentCountry");
		when(env.getInfusionsoftApptPhone()).thenReturn("_AppointmentLocationPhone");
		when(env.getInfusionsoftApptZip()).thenReturn("_AppointmentZip");
		when(env.getGoogleClientId()).thenReturn("");
		when(env.getGoogleClientSecrets()).thenReturn("");
		return env;
	}
}
