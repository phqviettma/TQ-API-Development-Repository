package com.tq.clinikosbmsync.lambda.util;

import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.simplybook.resp.DayInfo;

public class Util {
	private static final String startTime = "8:20";
	private static final String endTime = "18:00";
	public static DayInfo apptToDayInfo(AppointmentInfo appt) {
		DayInfo di = new DayInfo();
		appt.getAppointment_start();
		di.setDate(date);
		return null;
	}
}
