package com.tq.common.lambda.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "GCModifiedChannel")
public class GCModifiedChannel {
	private String sbmId;
	private Integer checkStatus;
	private long timeStamp;
	private String channelId;
	private String email;

	@DynamoDBRangeKey(attributeName = "channelId")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "Modified-Index", attributeName = "checkStatus")
	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	@DynamoDBIndexRangeKey(globalSecondaryIndexName = "Modified-Index", attributeName = "timeStamp")
	public long getTimestamp() {
		return timeStamp;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "Email-Index", attributeName = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTimestamp(long timestamp) {
		this.timeStamp = timestamp;
	}

	@DynamoDBHashKey(attributeName = "sbmId")
	public String getSbmId() {
		return sbmId;
	}

	public void setSbmId(String sbmId) {
		this.sbmId = sbmId;
	}

	public GCModifiedChannel(String sbmId, Integer checkStatus, long timeStamp) {

		this.sbmId = sbmId;
		this.checkStatus = checkStatus;
		this.timeStamp = timeStamp;
	}
	

	public GCModifiedChannel(String sbmId, Integer checkStatus, long timeStamp, String channelId, String email) {
		this.sbmId = sbmId;
		this.checkStatus = checkStatus;
		this.timeStamp = timeStamp;
		this.channelId = channelId;
		this.email = email;
	}

	public GCModifiedChannel() {

	}

	@Override
	public String toString() {
		return "GCModifiedChannel [sbmId=" + sbmId + ", checkStatus=" + checkStatus + ", timeStamp=" + timeStamp
				+ ", channelId=" + channelId + ", email=" + email + "]";
	}

}
