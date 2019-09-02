package com.tq.common.lambda.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ClinikoSyncStatus")
public class ClinikoSyncStatus {
	public static final String CHECK_KEY = "check";
	private String checkingItem = CHECK_KEY;
	private String apiKey;
	private Long timeStamp;
	private String latestTime;
	private boolean isReSync;

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "Cliniko-Index", attributeName = "checkingItem")
	public String getCheckingItem() {
		return checkingItem;
	}

	public void setCheckingItem(String checkingItem) {
		this.checkingItem = checkingItem;
	}

	@DynamoDBHashKey(attributeName = "apiKey")
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@DynamoDBIndexRangeKey(globalSecondaryIndexName = "Cliniko-Index", attributeName = "timeStamp")
	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@DynamoDBAttribute(attributeName = "latestTime")
	public String getLatestTime() {
		return latestTime;
	}

	public void setLatestTime(String latestTime) {
		this.latestTime = latestTime;
	}

	public boolean isReSync() {
		return isReSync;
	}

	public void setReSync(boolean isReSync) {
		this.isReSync = isReSync;
	}

	@Override
	public String toString() {
		return "ClinikoSyncStatus [checkingItem=" + checkingItem + ", apiKey=" + apiKey + ", timeStamp=" + timeStamp
				+ ", latestTime=" + latestTime + ", isReSync=" + isReSync + "]";
	}

	public ClinikoSyncStatus(String checkingItem, String apiKey, Long timeStamp, String latestTime) {

		this.checkingItem = checkingItem;
		this.apiKey = apiKey;
		this.timeStamp = timeStamp;
		this.latestTime = latestTime;
	}

	public ClinikoSyncStatus() {

	}

}
