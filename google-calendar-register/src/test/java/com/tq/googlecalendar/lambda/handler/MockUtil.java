package com.tq.googlecalendar.lambda.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.amazonaws.regions.Regions;
import com.tq.googlecalendar.lambda.context.Env;

public class MockUtil {
	public static Env mockEnv() {
		Env env = mock(Env.class);
		when(env.getRegions()).thenReturn(Regions.US_EAST_1);
		when(env.getAwsSecretAccessKey()).thenReturn("");
		when(env.getAwsAccessKeyId()).thenReturn("");
		when(env.getSimplyBookCompanyLogin()).thenReturn("truequit");
		when(env.getSimplyBookUser()).thenReturn("admin");
		when(env.getSimplyBookPassword()).thenReturn("");
		when(env.getSimplyBookServiceUrlLogin()).thenReturn("https://user-api.simplybook.asia/login");
		when(env.getSimplyBookAdminServiceUrl()).thenReturn("https://user-api.simplybook.asia/admin/");
		when(env.getInfusionSoftApiName()).thenReturn("https://fd940.infusionsoft.com/api/xmlrpc");
		when(env.getInfusionSoftApiKey()).thenReturn("");
		when(env.getInfusionSoftAppointmentTimeField()).thenReturn("_Appointmenttime");
		when(env.getInfusionSoftAppointmentLocationField()).thenReturn("_AppointmentLocation0");
		when(env.getInfusionSoftServiceProviderField()).thenReturn("_ServiceProvider");
		when(env.getInfusionSoftAppointmentInstructionField()).thenReturn("_AppointmentInstructions");
		when(env.getInfusionSoftCreateAppliedTag()).thenReturn(104);
		when(env.getInfusionSoftCancelAppliedTag()).thenReturn(106);
		when(env.getGoogleClientId()).thenReturn("");
		when(env.getGoogleClientSecrets()).thenReturn("");
		when(env.getGoogleNotifyDomain()).thenReturn("https://clinic.truequit.com/notifications/");

		return env;
	}
	


	
}
