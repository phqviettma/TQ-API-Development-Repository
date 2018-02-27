package com.tq.googlecalendar.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleErrorResp {
	private GoogleError error;
	private Integer id;
	private String jsonrpc = "2.0";

	public GoogleError getError() {
		return error;
	}

	public void setError(GoogleError error) {
		this.error = error;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJsonrpc() {
		return jsonrpc;
	}

	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}

	@Override
	public String toString() {
		return "GoogleErrorResp [error=" + error + ", id=" + id + ", jsonrpc=" + jsonrpc + "]";
	}

}
