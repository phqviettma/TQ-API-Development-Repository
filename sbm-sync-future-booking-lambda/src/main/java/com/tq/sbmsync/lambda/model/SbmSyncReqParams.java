package com.tq.sbmsync.lambda.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SbmSyncReqParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4308938699295240003L;
	private String googleCalendarEmail;
	private String clinikoApiKey;

	public String getGoogleCalendarEmail() {
		return googleCalendarEmail;
	}

	public void setGoogleCalendarEmail(String googleCalendarEmail) {
		this.googleCalendarEmail = googleCalendarEmail;
	}

	public String getClinikoApiKey() {
		return clinikoApiKey;
	}

	public void setClinikoApiKey(String clinikoApiKey) {
		this.clinikoApiKey = clinikoApiKey;
	}

	@Override
	public String toString() {
		return "SbmSyncReqParams [googleCalendarEmail=" + googleCalendarEmail + ", clinikoApiKey=" + clinikoApiKey
				+ "]";
	}

}
