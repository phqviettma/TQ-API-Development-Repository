package com.tq.googlecalendar.resp;

import java.io.Serializable;

public class ApiResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1187780154410358818L;
	private String entity;
	private Integer statusCode;

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public String toString() {
		return "ApiResponse [entity=" + entity + ", statusCode=" + statusCode + "]";
	}

	public ApiResponse(String entity, Integer statusCode) {

		this.entity = entity;
		this.statusCode = statusCode;
	}

	public ApiResponse() {

	}

}
