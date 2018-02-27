package com.tq.googlecalendar.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Start implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8092009487873623881L;
	private String dateTime;
	private String timeZone;

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	@Override
	public String toString() {
		return "Start [dateTime=" + dateTime + ", timeZone=" + timeZone + "]";
	}

	public Start(String dateTime, String timeZone) {

		this.dateTime = dateTime;
		this.timeZone = timeZone;
	}

	public Start() {

	}

}
