package com.tq.simplybook.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeZoneDetail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6866199121256579861L;
	private String id;
	private CompanyTimeZone result = null;
	private String jsonrpc;
	public String getId() {
		return id;
	}
	public CompanyTimeZone getResult() {
		return result;
	}
	public String getJsonrpc() {
		return jsonrpc;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setResult(CompanyTimeZone result) {
		this.result = result;
	}
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}
	@Override
	public String toString() {
		return "TimeZoneDetail [id=" + id + ", result=" + result + ", jsonrpc=" + jsonrpc + "]";
	}
	
	
}
