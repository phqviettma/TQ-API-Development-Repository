package com.tq.googlecalendar.model;

import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;

public class GeneralAppt {

	private String appointmentStart;
	private String appointmentEnd;
	private String taggedApptId;
	private SbmGoogleCalendar sbmGoogleCalendar;

	public String getAppointmentStart() {
		return appointmentStart;
	}

	public void setAppointmentStart(String appointmentStart) {
		this.appointmentStart = appointmentStart;
	}

	public String getAppointmentEnd() {
		return appointmentEnd;
	}

	public void setAppointmentEnd(String appointmentEnd) {
		this.appointmentEnd = appointmentEnd;
	}

	public String getTaggedApptId() {
		return taggedApptId;
	}

	public void setTaggedApptId(String taggedAppt) {
		this.taggedApptId = taggedAppt;
	}

	public SbmGoogleCalendar getSbmGoogleCalendar() {
		return sbmGoogleCalendar;
	}

	public void setSbmGoogleCalendar(SbmGoogleCalendar sbmGoogleCalendar) {
		this.sbmGoogleCalendar = sbmGoogleCalendar;
	}

	public GeneralAppt(String appointmentStart, String appointmentEnd) {

		this.appointmentStart = appointmentStart;
		this.appointmentEnd = appointmentEnd;
	}

	public GeneralAppt(String appointmentStart, String appointmentEnd, String taggedAppt,
			SbmGoogleCalendar sbmGoogleCalendar) {
		this.appointmentStart = appointmentStart;
		this.appointmentEnd = appointmentEnd;
		this.taggedApptId = taggedAppt;
		this.sbmGoogleCalendar = sbmGoogleCalendar;
	}

	public GeneralAppt() {

	}

	@Override
	public String toString() {
		return "GeneralAppt [appointmentStart=" + appointmentStart + ", appointmentEnd=" + appointmentEnd + "]";
	}

}
