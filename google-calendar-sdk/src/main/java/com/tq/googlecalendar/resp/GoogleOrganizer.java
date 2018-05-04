package com.tq.googlecalendar.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleOrganizer {
	private String email;
	private Boolean self;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getSelf() {
		return self;
	}

	public void setSelf(Boolean self) {
		this.self = self;
	}

	@Override
	public String toString() {
		return "GoogleOrganizer [email=" + email + ", self=" + self + "]";
	}

}