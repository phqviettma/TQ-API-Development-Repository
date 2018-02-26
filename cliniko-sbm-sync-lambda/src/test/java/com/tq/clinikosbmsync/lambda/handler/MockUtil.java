package com.tq.clinikosbmsync.lambda.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.tq.simplybook.context.Env;


public class MockUtil {
	public static Env mockEnv() {
		Env env = mock(Env.class);
		when(env.getRegions()).thenReturn(Regions.US_EAST_1);
		when(env.getAwsSecretAccessKey()).thenReturn("");
		when(env.getAwsAccessKeyId()).thenReturn("");
		when(env.getSimplyBookCompanyLogin()).thenReturn("canhcanh");
		when(env.getSimplyBookUser()).thenReturn("admin");
		when(env.getSimplyBookPassword()).thenReturn("");
		when(env.getSimplyBookServiceUrlLogin()).thenReturn("https://user-api.simplybook.asia/login");
		when(env.getSimplyBookAdminServiceUrl()).thenReturn("https://user-api.simplybook.asia/admin/");
		when(env.getInfusionSoftApiName()).thenReturn("https://bh321.infusionsoft.com/api/xmlrpc");
		when(env.getInfusionSoftApiKey()).thenReturn("");
		when(env.getInfusionSoftAppointmentTimeField()).thenReturn("_Appointmenttime");
		when(env.getInfusionSoftAppointmentLocationField()).thenReturn("_AppointmentLocation0");
		when(env.getInfusionSoftServiceProviderField()).thenReturn("_ServiceProvider");
		when(env.getInfusionSoftAppointmentInstructionField()).thenReturn("_AppointmentInstructions");
		when(env.getInfusionSoftCreateAppliedTag()).thenReturn(104);
		when(env.getInfusionSoftCancelAppliedTag()).thenReturn(106);
		when(env.getCliniko_start_time()).thenReturn("09:00");
		when(env.getCliniko_end_time()).thenReturn("18:00");
		when(env.getClinikoApiKey()).thenReturn("");
		Map<String, String> variable = new HashMap<String, String>();
		variable.put("SBM_CLINIKO_MAPPING_1",
				"{'event_id': '2', 'unit_id': '3', 'practitionerId': '89589', 'businessId': '58837'}");
		variable.put("SBM_CLINIKO_MAPPING_2",
				"{'event_id': '128', 'unit_id': '2', 'practitionerId': '89589', 'businessId': '58837'}");
		variable.put("SBM_CLINIKO_MAPPING_3",
				"{'event_id': '2', 'unit_id': '2', 'practitionerId': '89589', 'businessId': '58837'}");
		variable.put("SBM_CLINIKO_MAPPING_3",
				"{'event_id': '1', 'unit_id': '1', 'practitionerId': '89589', 'businessId': '58837'}");
		variable.put(env.getInfusionSoftApiKey(), "");
		when(env.getAllEnvVariables()).thenReturn(variable);
		return env;
	}
	


	
}
