package com.tq.calendar.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -980735082746336645L;
	private String client_id;
	private String client_secret;
	private String refresh_token;
	private String grant_type;

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	@Override
	public String toString() {
		return "TokenReq [client_id=" + client_id + ", client_secret=" + client_secret + ", refresh_token="
				+ refresh_token + ", grant_type=" + grant_type + "]";
	}

	public TokenReq(String client_id, String client_secret, String refresh_token) {

		this.client_id = client_id;
		this.client_secret = client_secret;
		this.refresh_token = refresh_token;
		this.grant_type = "refresh_token";
	}

	public TokenReq() {

	}

}
