package com.tq.googlecalendar.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleCalendarSettingsInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3879426892493225751L;
	private String kind;
	private String id;
	private String value;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "GoogleCalendarSettingsInfo [kind=" + kind + ", id=" + id + ", value=" + value + "]";
	}

}
