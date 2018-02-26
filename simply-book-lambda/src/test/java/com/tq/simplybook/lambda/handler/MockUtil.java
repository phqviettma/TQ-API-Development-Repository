package com.tq.simplybook.lambda.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import com.tq.simplybook.context.Env;

public class MockUtil {
	public static Env mockEnv() {
		Env env = mock(Env.class);
		when(env.getSimplyBookCompanyLogin()).thenReturn("truequit");
		when(env.getSimplyBookUser()).thenReturn("admin");
		when(env.getSimplyBookPassword()).thenReturn("");
		when(env.getSimplyBookSecretKey())
				.thenReturn("");
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
		when(env.getCliniko_standard_appointment()).thenReturn(252503);
		when(env.getClinikoPatientId()).thenReturn(46101691);
		when(env.getCliniko_end_time()).thenReturn("18:00");
		when(env.getCliniko_start_time()).thenReturn("09:00");
		when(env.getInfusionftAppointmentDate()).thenReturn("_AppointmentDate2");
		when(env.getInfusionsoftPractitionerFirstName()).thenReturn("_PractitionerFirstName");
		when(env.getInfusionsoftPractitionerFirstName()).thenReturn("_PractitionerLastName");
		Map<String, String> variable = new HashMap<String, String>();
		variable.put("SBM_CLINIKO_MAPPING_1",
				"{'event_id': '2', 'unit_id': '3', 'practitionerId': '87313', 'businessId': '57535'}");
		variable.put("SBM_CLINIKO_MAPPING_2",
				"{'event_id': '128', 'unit_id': '2', 'practitionerId': '87313', 'businessId': '57535'}");
		variable.put("SBM_CLINIKO_MAPPING_3",
				"{'event_id': '2', 'unit_id': '2', 'practitionerId': '87313', 'businessId': '57535'}");
		variable.put("SBM_CLINIKO_MAPPING_3",
				"{'event_id': '1', 'unit_id': '1', 'practitionerId': '87313', 'businessId': '57535'}");
		variable.put(env.getInfusionSoftApiKey(), "");
		when(env.getAllEnvVariables()).thenReturn(variable);
		when(env.getGoogleClientId())
				.thenReturn("");
		when(env.getGoogleClientSecrets()).thenReturn("");
		return env;
	}
}
