package com.tq.common.lambda.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "GCModifiedChannel")
public class GCModifiedChannel {
	private String googleCalendarId;
	private Integer checkingStatus;
	private long timeStamp;
	private String email;
	private String channelId;

	@DynamoDBHashKey(attributeName = "googleCalendarId")
	public String getGoogleCalendarId() {
		return googleCalendarId;
	}

	public void setGoogleCalendarId(String googleCalendarId) {
		this.googleCalendarId = googleCalendarId;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "Status-Index", attributeName = "checkingStatus")
	public Integer getCheckingStatus() {
		return checkingStatus;
	}

	public void setCheckingStatus(Integer checkStatus) {
		this.checkingStatus = checkStatus;
	}

	@DynamoDBIndexRangeKey(globalSecondaryIndexName = "Status-Index", attributeName = "timeStamp")
	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "Email-Index", attributeName = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@DynamoDBAttribute(attributeName = "channelId")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public GCModifiedChannel() {

	}

	public GCModifiedChannel(String googleCalendarId, Integer checkingStatus, long timeStamp, String email,
			String channelId) {

		this.googleCalendarId = googleCalendarId;
		this.checkingStatus = checkingStatus;
		this.timeStamp = timeStamp;
		this.email = email;
		this.channelId = channelId;
	}

}
