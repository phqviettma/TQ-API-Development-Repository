package com.tq.cliniko.utils;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.service.ClinikoAppointmentService;

public class ClinikoUtils {
	
	public static boolean isExpiredOrNotAuthorized(ClinikoAppointmentService clinikoApptService) throws ClinikoSDKExeption {
		Settings settings = clinikoApptService.getAllSettings();
		if (settings == null || settings.getAccount() == null) {
			return true;
		}
		return false;
	}
}
