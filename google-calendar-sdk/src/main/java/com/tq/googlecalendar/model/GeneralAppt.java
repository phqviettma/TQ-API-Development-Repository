package com.tq.googlecalendar.model;

import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.googlecalendar.resp.Items;

public class GeneralAppt {

	private String appointmentStart;
	private String appointmentEnd;
	private Items googleEvent;
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

	public SbmGoogleCalendar getSbmGoogleCalendar() {
		return sbmGoogleCalendar;
	}

	public void setSbmGoogleCalendar(SbmGoogleCalendar sbmGoogleCalendar) {
		this.sbmGoogleCalendar = sbmGoogleCalendar;
	}

	public Items getGoogleEvent() {
		return googleEvent;
	}

	public void setGoogleEvent(Items googleEvent) {
		this.googleEvent = googleEvent;
	}

	public GeneralAppt(String appointmentStart, String appointmentEnd) {

		this.appointmentStart = appointmentStart;
		this.appointmentEnd = appointmentEnd;
	}



	public GeneralAppt(String appointmentStart, String appointmentEnd,
			SbmGoogleCalendar sbmGoogleCalendar) {
	
		this.appointmentStart = appointmentStart;
		this.appointmentEnd = appointmentEnd;
		this.sbmGoogleCalendar = sbmGoogleCalendar;
	}

	public GeneralAppt(String appointmentStart, String appointmentEnd, Items googleEvent) {
		this.appointmentStart = appointmentStart;
		this.appointmentEnd = appointmentEnd;
		this.googleEvent = googleEvent;
	}

	public GeneralAppt() {

	}

	@Override
	public String toString() {
		return "GeneralAppt [appointmentStart=" + appointmentStart + ", appointmentEnd=" + appointmentEnd + "]";
	}

}
