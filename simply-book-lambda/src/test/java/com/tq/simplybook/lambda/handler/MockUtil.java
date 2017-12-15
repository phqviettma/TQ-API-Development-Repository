package com.tq.simplybook.lambda.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import com.tq.simplybook.context.Env;

public class MockUtil {
	public static Env mockEnv() {
		Env env = mock(Env.class);
		when(env.getSimplyBookCompanyLogin()).thenReturn("vuongtran");
		when(env.getSimplyBookUser()).thenReturn("admin");
		when(env.getSimplyBookPassword()).thenReturn("y#EDyMA3E3");
		when(env.getSimplyBookServiceUrlLogin()).thenReturn("https://user-api.simplybook.me/login");
		when(env.getSimplyBookAdminServiceUrl()).thenReturn("https://user-api.simplybook.me/admin/");
		when(env.getInfusionSoftApiName()).thenReturn("https://bh321.infusionsoft.com/api/xmlrpc");
		when(env.getInfusionSoftApiKey()).thenReturn("a820973712db06a648c13713ef4d5fea");
		when(env.getInfusionSoftAppointmentTimeField()).thenReturn("_Appointmenttime");
		when(env.getInfusionSoftAppointmentLocationField()).thenReturn("_AppointmentLocation0");
		when(env.getInfusionSoftServiceProviderField()).thenReturn("_ServiceProvider");
		when(env.getInfusionSoftAppointmentInstructionField()).thenReturn("_AppointmentInstructions");
		when(env.getInfusionSoftCreateAppliedTag()).thenReturn(104);
		when(env.getInfusionSoftCancelAppliedTag()).thenReturn(106);
		when(env.getClinikoApiKey()).thenReturn("2b2f8a6c0238919e66b81c089da283d2");
		when(env.getCliniko_standard_appointment()).thenReturn(252503);
		when(env.getClinikoPatientId()).thenReturn(46101691);
		when(env.getCliniko_end_time()).thenReturn("18:00");
		when(env.getCliniko_start_time()).thenReturn("09:00");
		Map<String, String> variable = new HashMap<String, String>();
		variable.put("SBM_CLINIKO_MAPPING_1","{'event_id': '2', 'unit_id': '3', 'practitionerId': '87313', 'businessId': '57535'}");
		variable.put("SBM_CLINIKO_MAPPING_2", "{'event_id': '128', 'unit_id': '2', 'practitionerId': '87313', 'businessId': '57535'}");
		variable.put("SBM_CLINIKO_MAPPING_3", "{'event_id': '2', 'unit_id': '2', 'practitionerId': '87313', 'businessId': '57535'}");
		variable.put("SBM_CLINIKO_MAPPING_3", "{'event_id': '1', 'unit_id': '1', 'practitionerId': '87313', 'businessId': '57535'}");
		variable.put(env.getInfusionSoftApiKey(), "a820973712db06a648c13713ef4d5fea");
		when(env.getAllEnvVariables()).thenReturn(variable);
		return env;
	}
}
