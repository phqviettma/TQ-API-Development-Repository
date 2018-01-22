package com.tq.calendar.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WatchEventResp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3233146108521873793L;
	private String kind;
	private String id;
	private String resourceId;
	private String resourceUri;
	private String token;
	private Long expiration;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWatchResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceUri() {
		return resourceUri;
	}

	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getExpiration() {
		return expiration;
	}

	public void setExpiration(Long expiration) {
		this.expiration = expiration;
	}

	@Override
	public String toString() {
		return "WatchEventResp [kind=" + kind + ", id=" + id + ", resourceId=" + resourceId + ", resourceUri="
				+ resourceUri + ", token=" + token + ", expiration=" + expiration + "]";
	}

}
