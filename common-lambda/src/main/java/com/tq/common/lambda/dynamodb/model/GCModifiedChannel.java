package com.tq.common.lambda.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
@DynamoDBTable(tableName = "GCModifiedChannel")
public class GCModifiedChannel {
	private String channelId;
	private Integer checkStatus;
	private long timeStamp;
	

	@DynamoDBHashKey(attributeName = "channelId")
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
	@DynamoDBIndexRangeKey(globalSecondaryIndexName = "Modified-Index",attributeName = "timeStamp")
	public long getTimestamp() {
		return timeStamp;
	}

	public void setTimestamp(long timestamp) {
		this.timeStamp = timestamp;
	}

	public GCModifiedChannel(String channelId, Integer checkStatus, long timeStamp) {

		this.channelId = channelId;
		this.checkStatus = checkStatus;
		this.timeStamp = timeStamp;
	}

	public GCModifiedChannel() {

	}

	@Override
	public String toString() {
		return "GCModifiedChannel [channelId=" + channelId + ", checkStatus=" + checkStatus + ", timestamp=" + timeStamp
				+ "]";
	}
	

}
