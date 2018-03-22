package com.tq.googlecalendar.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WatchEventReq implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 5440077663506635172L;
	private String id;
	private String token;
	private String type;
	private String address;
	private Params params;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Params getParams() {
		return params;
	}

	public void setParams(Params params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "WatchEventReq [id=" + id + ", token=" + token + ", type=" + type + ", address=" + address + "]";
	}

	public WatchEventReq(String id, String type, String address, Params params) {
		this.id = id;
		this.type = type;
		this.address = address;
		this.params = params;
	}

	public WatchEventReq() {

	}

}
