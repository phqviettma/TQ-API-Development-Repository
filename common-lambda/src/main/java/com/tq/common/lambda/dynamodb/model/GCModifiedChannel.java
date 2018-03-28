package com.tq.common.lambda.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
@DynamoDBTable(tableName = "GCModifiedChannel")
public class GCModifiedChannel {
	private String channelId;
	private Integer status;
	private long timeStamp;
	

	@DynamoDBHashKey(attributeName = "channelId")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "Modified-Index", attributeName = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	@DynamoDBRangeKey(attributeName = "timeStamp")
	public long getTimestamp() {
		return timeStamp;
	}

	public void setTimestamp(long timestamp) {
		this.timeStamp = timestamp;
	}

	public GCModifiedChannel(String channelId, Integer status, long timeStamp) {

		this.channelId = channelId;
		this.status = status;
		this.timeStamp = timeStamp;
	}

	public GCModifiedChannel() {

	}

	@Override
	public String toString() {
		return "GCModifiedChannel [channelId=" + channelId + ", status=" + status + ", timestamp=" + timeStamp
				+ "]";
	}
	

}
