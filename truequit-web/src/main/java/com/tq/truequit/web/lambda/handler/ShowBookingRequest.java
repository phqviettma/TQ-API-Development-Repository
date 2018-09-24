package com.tq.truequit.web.lambda.handler;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tq.truequit.web.lambda.request.RequestParams;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShowBookingRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4829319837121875724L;
	private String action;
	private RequestParams params;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public RequestParams getParams() {
		return params;
	}

	public void setParams(RequestParams params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "ShowBookingRequest [action=" + action + ", params=" + params + "]";
	}

}
