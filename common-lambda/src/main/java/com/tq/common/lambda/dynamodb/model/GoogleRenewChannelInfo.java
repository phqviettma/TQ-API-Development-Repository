package com.tq.common.lambda.dynamodb.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "GoogleRenewChannelInfo")
public class GoogleRenewChannelInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -509441797494676490L;
	private Long checkDay;
	private Long expirationTime;
	private String channelId;
	private String refreshToken;
	private String resourceId;
	private String googleEmail;
	private Long lastCheckingTime;

	@DynamoDBAttribute(attributeName = "expirationTime")
	public Long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Long expirationTime) {
		this.expirationTime = expirationTime;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "Channel-index",attributeName = "channelId")
	@DynamoDBRangeKey(attributeName = "channelId")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public GoogleRenewChannelInfo() {

	}

	@DynamoDBHashKey(attributeName = "checkDay")
	public Long getCheckDay() {
		return checkDay;
	}

	public void setCheckDay(Long checkDay) {
		this.checkDay = checkDay;
	}

	@DynamoDBAttribute(attributeName = "refreshToken")
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@DynamoDBAttribute(attributeName = "resourceId")
	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	@DynamoDBAttribute(attributeName = "googleEmail")
	public String getGoogleEmail() {
		return googleEmail;
	}

	public void setGoogleEmail(String googleEmail) {
		this.googleEmail = googleEmail;
	}

	@DynamoDBAttribute(attributeName = "lastCheckingTime")
	public Long getLastCheckingTime() {
		return lastCheckingTime;
	}

	public void setLastCheckingTime(Long lastCheckingTime) {
		this.lastCheckingTime = lastCheckingTime;
	}

	public GoogleRenewChannelInfo(Long checkDay, Long expirationTime, String channelId, String refreshToken,
			String resourceId, String googleEmail, Long lastCheckingTime) {
		this.checkDay = checkDay;
		this.expirationTime = expirationTime;
		this.channelId = channelId;
		this.refreshToken = refreshToken;
		this.resourceId = resourceId;
		this.googleEmail = googleEmail;
		this.lastCheckingTime = lastCheckingTime;

	}

	@Override
	public String toString() {
		return "GoogleRenewChannelInfo [checkDay=" + checkDay + ", expirationTime=" + expirationTime + ", channelId="
				+ channelId + ", refreshToken=" + refreshToken + ", resourceId=" + resourceId + ", googleEmail="
				+ googleEmail + ", lastCheckingTime=" + lastCheckingTime + "]";
	}

}
