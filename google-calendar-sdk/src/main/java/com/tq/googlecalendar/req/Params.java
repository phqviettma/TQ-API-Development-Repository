package com.tq.googlecalendar.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Params implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 2854382217698249379L;
	private String ttl;

	public String getTtl() {
		return ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

	@Override
	public String toString() {
		return "Params [ttl=" + ttl + "]";
	}

	public Params(String ttl) {

		this.ttl = ttl;
	}

	public Params() {

	}

}
