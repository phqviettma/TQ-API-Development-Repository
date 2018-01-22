package com.tq.calendar.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8646932792585586889L;
	private String access_token;
	private Integer expires_in;
	private String id_token;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public Integer getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}

	public String getId_token() {
		return id_token;
	}

	public void setId_token(String id_token) {
		this.id_token = id_token;
	}

	@Override
	public String toString() {
		return "TokenResp [access_token=" + access_token + ", expires_in=" + expires_in + ", id_token=" + id_token
				+ "]";
	}

}
