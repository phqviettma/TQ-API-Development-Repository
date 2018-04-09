package com.tq.common.lambda.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LambdaStatusResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3191387301470075777L;
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

	@Override
	public String toString() {
		return "LambdaStatusResponse [succeeded=" + succeeded + ", status=" + status + "]";
	}

	public LambdaStatusResponse(boolean succeeded, String status) {

		this.succeeded = succeeded;
		this.status = status;
	}

	public LambdaStatusResponse() {

	}

}
