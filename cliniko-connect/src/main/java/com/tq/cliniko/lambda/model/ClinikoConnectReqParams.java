package com.tq.cliniko.lambda.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClinikoConnectReqParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4990808343304857797L;
	private String apiKey;
	private String practitionerEmail;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getPractitionerEmail() {
		return practitionerEmail;
	}

	public void setPractitionerEmail(String practitionerEmail) {
		this.practitionerEmail = practitionerEmail;
	}

	@Override
	public String toString() {
		return "ClinikoConnectReqParams [apiKey=" + apiKey + ", practitionerEmail=" + practitionerEmail + "]";
	}

}
