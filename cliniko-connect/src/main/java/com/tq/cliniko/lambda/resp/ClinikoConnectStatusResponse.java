package com.tq.cliniko.lambda.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClinikoConnectStatusResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1278768251999330621L;
	private boolean succeeded;
	private String status;

	public boolean isSucceeded() {
		return succeeded;
	}

	public void setSucceeded(boolean succeeded) {
		this.succeeded = succeeded;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ClinikoConnectStatusResponse(boolean succeeded, String status) {
		this.succeeded = succeeded;
		this.status = status;
	}

	public ClinikoConnectStatusResponse() {
	}

	@Override
	public String toString() {
		return "ClinikoConnectStatusResponse [succeeded=" + succeeded + ", status=" + status + "]";
	}

}
