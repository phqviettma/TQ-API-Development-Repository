package com.tq.googlecalendar.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleCalendarItems implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 335900477466714894L;
	private String id;
	private String summary;
	private String timeZone;
	private String accessRole;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getAccessRole() {
		return accessRole;
	}

	public void setAccessRole(String accessRole) {
		this.accessRole = accessRole;
	}

	@Override
	public String toString() {
		return "GoogleCalendarItems [id=" + id + ", summary=" + summary + ", timeZone=" + timeZone + ", accessRole="
				+ accessRole + "]";
	}

}
