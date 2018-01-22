package com.tq.calendarsbmsync.lambda.model;

import java.io.Serializable;

public class SyncMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -165675351226620053L;
	private String googleChannelId;
	private String googleChannelToken;
	private String googleChannelExpiration;
	private String googleResourceId;
	private String googleResourceUri;
	private String googleResourceState;
	private Integer googleMessageNumber;

	public String getGoogleChannelId() {
		return googleChannelId;
	}

	public void setGoogleChannelId(String googleChannelId) {
		this.googleChannelId = googleChannelId;
	}

	public String getGoogleChannelToken() {
		return googleChannelToken;
	}

	public void setGoogleChannelToken(String googleChannelToken) {
		this.googleChannelToken = googleChannelToken;
	}

	public String getGoogleChannelExpiration() {
		return googleChannelExpiration;
	}

	public void setGoogleChannelExpiration(String googleChannelExpiration) {
		this.googleChannelExpiration = googleChannelExpiration;
	}

	public String getGoogleResourceId() {
		return googleResourceId;
	}

	public void setGoogleResourceId(String googleResourceId) {
		this.googleResourceId = googleResourceId;
	}

	public String getGoogleResourceUri() {
		return googleResourceUri;
	}

	public void setGoogleResourceUri(String googleResourceUri) {
		this.googleResourceUri = googleResourceUri;
	}

	public String getGoogleResourceState() {
		return googleResourceState;
	}

	public void setGoogleResourceState(String googleResourceState) {
		this.googleResourceState = googleResourceState;
	}

	public Integer getGoogleMessageNumber() {
		return googleMessageNumber;
	}

	public void setGoogleMessageNumber(Integer googleMessageNumber) {
		this.googleMessageNumber = googleMessageNumber;
	}

	@Override
	public String toString() {
		return "SyncMessage [googleChannelId=" + googleChannelId + ", googleChannelToken=" + googleChannelToken
				+ ", googleChannelExpiration=" + googleChannelExpiration + ", googleResourceId=" + googleResourceId
				+ ", googleResourceUri=" + googleResourceUri + ", googleResourceState=" + googleResourceState
				+ ", googleMessageNumber=" + googleMessageNumber + "]";
	}

}
