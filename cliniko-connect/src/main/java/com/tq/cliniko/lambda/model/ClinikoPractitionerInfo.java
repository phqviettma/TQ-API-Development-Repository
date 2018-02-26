package com.tq.cliniko.lambda.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClinikoPractitionerInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4053835909375070005L;
	private String apiKey;
	private String status;

	

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ClinikoPractitionerInfo [ApiKey=" + apiKey + ", status=" + status + "]";
	}

}
