package com.tq.cliniko.lambda.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClinikoPractitionerConnectReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4053835909375070005L;
	private ClinikoConnectReqParams params;
	private String action;

	public ClinikoConnectReqParams getParams() {
		return params;
	}

	public void setParams(ClinikoConnectReqParams params) {
		this.params = params;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "ClinikoPractitionerConnectReq [params=" + params + ", action=" + action + "]";
	}

}
