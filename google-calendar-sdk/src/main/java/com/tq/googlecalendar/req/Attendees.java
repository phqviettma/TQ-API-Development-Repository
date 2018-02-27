package com.tq.googlecalendar.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attendees implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1999027776680821551L;
	private String email;
	private String displayName;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Attendees(String email, String displayName) {

		this.email = email;
		this.displayName = displayName;
	}

	public Attendees() {

	}

	@Override
	public String toString() {
		return "Attendees [email=" + email + ", displayName=" + displayName + "]";
	}

}
