package com.tq.common.lambda.dynamodb.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "GoogleCalendarSbmSync")
public class GoogleCalendarSbmSync implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3130527856173772507L;
	private String sbmId;
	private String email;
	private String googleCalendarId;
	private String lastname;
	private String firstname;
	private String refreshToken;
	private String nextSyncToken;
	private String googleEmail;
	private String nextPageToken;
	private String gcWatchResourceId;
	private String lastQueryTimeMin;
	private String channelId;
	private boolean isResync;

	@DynamoDBAttribute(attributeName = "gcWatchResourceId")
	public String getGcWatchResourceId() {
		return gcWatchResourceId;
	}

	public void setGcWatchResourceId(String resourceId) {
		this.gcWatchResourceId = resourceId;
	}

	@DynamoDBAttribute(attributeName = "googleCalendarId")
	public String getGoogleCalendarId() {
		return googleCalendarId;
	}

	public void setGoogleCalendarId(String googleCalendarId) {
		this.googleCalendarId = googleCalendarId;
	}

	@DynamoDBAttribute(attributeName = "sbmId")
	public String getSbmId() {
		return sbmId;
	}

	public void setLastQueryTimeMin(String lastQueryTimeMin) {
		this.lastQueryTimeMin = lastQueryTimeMin;
	}

	@DynamoDBAttribute(attributeName = "lastQueryTimeMin")
	public String getLastQueryTimeMin() {
		return lastQueryTimeMin;
	}

	public void setSbmId(String sbmId) {
		this.sbmId = sbmId;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "Email-Index", attributeName = "email")
	public String getEmail() {
		return email;
	}

	@DynamoDBAttribute(attributeName = "nextPageToken")
	public String getNextPageToken() {
		return nextPageToken;
	}

	@DynamoDBHashKey(attributeName = "channelId")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@DynamoDBAttribute(attributeName = "lastname")
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@DynamoDBAttribute(attributeName = "firstname")
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@DynamoDBAttribute(attributeName = "refreshToken")
	public String getRefreshToken() {
		return refreshToken;
	}

	@DynamoDBAttribute(attributeName = "nextSyncToken")
	public String getNextSyncToken() {
		return nextSyncToken;
	}

	@DynamoDBAttribute(attributeName = "googleEmail")
	public String getGoogleEmail() {
		return googleEmail;
	}

	public void setGoogleEmail(String googleEmail) {
		this.googleEmail = googleEmail;
	}

	public void setNextSyncToken(String nextSyncToken) {
		this.nextSyncToken = nextSyncToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public boolean isResync() {
		return isResync;
	}

	public void setResync(boolean isResync) {
		this.isResync = isResync;
	}

	public GoogleCalendarSbmSync(String sbmId, String googleEmail, String email, String lastname, String firstname,
			String refreshToken, String nextSyncToken, String gcResourceId) {
		this.sbmId = sbmId;
		this.email = email;
		this.lastname = lastname;
		this.firstname = firstname;
		this.refreshToken = refreshToken;
		this.nextSyncToken = nextSyncToken;
		this.googleEmail = googleEmail;
		this.gcWatchResourceId = gcResourceId;
	}

	public GoogleCalendarSbmSync(String sbmId, String email, String googleCalendarId, String lastname, String firstname,
			String refreshToken, String googleEmail, String gcWatchResourceId, String channelId) {
		this.sbmId = sbmId;
		this.email = email;
		this.googleCalendarId = googleCalendarId;
		this.lastname = lastname;
		this.firstname = firstname;
		this.refreshToken = refreshToken;
		this.googleEmail = googleEmail;
		this.gcWatchResourceId = gcWatchResourceId;
		this.channelId = channelId;
	}

	public GoogleCalendarSbmSync() {

	}

	@Override
	public String toString() {
		return "GoogleCalendarSbmSync [sbmId=" + sbmId + ", email=" + email + ", googleCalendarId=" + googleCalendarId
				+ ", lastname=" + lastname + ", firstname=" + firstname + ", refreshToken=" + refreshToken
				+ ", nextSyncToken=" + nextSyncToken + ", googleEmail=" + googleEmail + ", nextPageToken="
				+ nextPageToken + ", gcWatchResourceId=" + gcWatchResourceId + ", lastQueryTimeMin=" + lastQueryTimeMin
				+ "]";
	}

}
