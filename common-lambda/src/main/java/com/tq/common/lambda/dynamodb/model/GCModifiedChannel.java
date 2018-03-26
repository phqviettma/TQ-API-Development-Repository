package com.tq.common.lambda.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;

public class GCModifiedChannel {
	private String channelId;
	private Boolean modified;

	@DynamoDBHashKey(attributeName = "channelId")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@DynamoDBAttribute(attributeName = "modified")
	public Boolean getModified() {
		return modified;
	}

	public void setModified(Boolean modified) {
		this.modified = modified;
	}

	@Override
	public String toString() {
		return "GCModifiedChannel [channelId=" + channelId + ", modified=" + modified + "]";
	}

	public GCModifiedChannel(String channelId, Boolean modified) {

		this.channelId = channelId;
		this.modified = modified;
	}

	public GCModifiedChannel() {

	}

}
