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
	private String email;
	private String clinikoApiKey;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getClinikoApiKey() {
		return clinikoApiKey;
	}

	public void setClinikoApiKey(String clinikoApiKey) {
		this.clinikoApiKey = clinikoApiKey;
	}

	@Override
	public String toString() {
		return "SbmSyncReqParams [email=" + email + ", clinikoApiKey=" + clinikoApiKey + "]";
	}

}
