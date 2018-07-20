package com.tq.simplybook.lambda.util;

import java.util.Arrays;
import java.util.List;

import com.tq.common.lambda.utils.TimeUtils;

public class SbmInfUtil {


	public static String buildApppointmentTime(String start_date_time, String end_date_time) {
		String startTime = TimeUtils.getTimeFormatTwHour(start_date_time);
		String endTime = TimeUtils.getTimeFormatTwHour(end_date_time);
		return startTime + ((endTime == null || endTime.isEmpty() ? "" : " - " + endTime));
	}

	public static String buildAppointmentDate(String start_date_time) {
		String startDate = TimeUtils.extractDateSbm(start_date_time);

		return startDate == null ? "" : startDate;
	}

	public static String buildFirstName(String name) {
		String[] splitName = name.trim().split("\\s+");
		return splitName[0];
	}

	public static String buildLastName(String name) {
		List<String> lastName = Arrays.asList(name.trim().split("\\s+"));
		if (lastName.size() > 1) {
			return lastName.get(1);
		} else {
			return "";
		}
	}
}
