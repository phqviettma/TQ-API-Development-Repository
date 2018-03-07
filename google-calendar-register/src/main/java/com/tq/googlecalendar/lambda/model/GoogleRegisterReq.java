package com.tq.googlecalendar.lambda.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleRegisterReq implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 4213124658354140623L;
	private String action;
	private GoogleRegisterParams params;

	public String getAction() {
		return action;
	}

	public GoogleRegisterParams getParams() {
		return params;
	}

	public void setParams(GoogleRegisterParams params) {
		this.params = params;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public GoogleRegisterReq(String action, GoogleRegisterParams params) {

		this.action = action;
		this.params = params;
	}

	public GoogleRegisterReq() {

	}

	@Override
	public String toString() {
		return "GoogleRegisterReq [action=" + action + ", params=" + params + "]";
	}

}
