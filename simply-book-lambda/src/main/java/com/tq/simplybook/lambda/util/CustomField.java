package com.tq.simplybook.lambda.util;

import java.util.HashMap;
import java.util.Map;

import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.resp.BookingInfo;

public class CustomField {
	public static Map<String, String> buildInfCustomField(CountryItemService countryItemService, Env env,
			BookingInfo bookingInfo) {
		String infusionSoftAppointmentTimeField = env.getInfusionSoftAppointmentTimeField();
		String infusionSoftAppointmentLocationField = env.getInfusionSoftAppointmentLocationField();
		String infusionSoftServiceProviderField = env.getInfusionSoftServiceProviderField();
		String infusionSoftAppointmentInstructionField = env.getInfusionSoftAppointmentInstructionField();
		String infusionSoftAppointmentDateField = env.getInfusionftAppointmentDate();
		String infusionSoftPractitionerLastName = env.getInfusionsoftPractitionerLastName();
		String infusionsoftPractitionerFirstName = env.getInfusionsoftPractitionerFirstName();
		String infusionsoftAppointmentAddress1 = env.getInfusionsoftApptAddress1();
		String infusionsoftAppointmentAddress2 = env.getInfusionsoftApptAddress2();
		String infusionsoftAppointmentCity = env.getInfusionsoftApptCity();
		String infusionsoftAppointmentCountry = env.getInfusionsoftApptCountry();
		String infusionsoftAppointmentPhone = env.getInfusionsoftApptPhone();
		String infusionsoftAppointmentZip = env.getInfusionsoftApptZip();
		String infusionSoftPractitionerEmail = env.getInfusionsoftPractitionerEmail();
		String infusionsoftApptCountryName = countryItemService
				.queryCountryCode(bookingInfo.getLocation().getCountry_id());
		if (infusionsoftApptCountryName == null) {
			infusionsoftApptCountryName = bookingInfo.getLocation().getCountry_id();
		}
		Map<String, String> updateRecord = new HashMap<>();

		updateRecord.put(infusionSoftAppointmentTimeField,
				SbmInfUtil.buildApppointmentTime(bookingInfo.getStart_date_time(), bookingInfo.getEnd_date_time()));
		updateRecord.put(infusionSoftAppointmentLocationField,
				bookingInfo.getLocation() == null ? "" : String.valueOf(bookingInfo.getLocation().getTitle()));
		updateRecord.put(infusionSoftServiceProviderField, bookingInfo.getUnit_name());
		updateRecord.put(infusionSoftAppointmentInstructionField, bookingInfo.getLocation().getDescription());
		updateRecord.put(infusionSoftAppointmentDateField,
				SbmInfUtil.buildAppointmentDate(bookingInfo.getStart_date_time()));
		updateRecord.put(infusionsoftPractitionerFirstName, SbmInfUtil.buildFirstName(bookingInfo.getUnit_name()));
		updateRecord.put(infusionSoftPractitionerLastName, SbmInfUtil.buildLastName(bookingInfo.getUnit_name()));
		updateRecord.put(infusionsoftAppointmentAddress1, bookingInfo.getLocation().getAddress1());
		updateRecord.put(infusionsoftAppointmentAddress2, bookingInfo.getLocation().getAddress2());
		updateRecord.put(infusionsoftAppointmentCity, bookingInfo.getLocation().getCity());
		updateRecord.put(infusionsoftAppointmentCountry, infusionsoftApptCountryName);
		updateRecord.put(infusionsoftAppointmentPhone, bookingInfo.getLocation().getPhone());
		updateRecord.put(infusionsoftAppointmentZip, bookingInfo.getLocation().getZip());
		updateRecord.put(infusionSoftPractitionerEmail, bookingInfo.getUnit_email());
		return updateRecord;
	}
}
