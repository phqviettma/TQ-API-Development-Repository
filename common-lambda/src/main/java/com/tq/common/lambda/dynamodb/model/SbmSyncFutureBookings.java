package com.tq.common.lambda.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "SbmSyncFutureBookings")
public class SbmSyncFutureBookings {
	private String sbmId;
	private String clinikoApiKey;
	private String email;
	private Integer syncStatus;
	private Long timeStamp;

	@DynamoDBHashKey(attributeName = "sbmId")
	public String getSbmId() {
		return sbmId;
	}

	public void setSbmId(String sbmId) {
		this.sbmId = sbmId;
	}

	@DynamoDBAttribute(attributeName = "clinikoApiKey")
	public String getClinikoApiKey() {
		return clinikoApiKey;
	}

	public void setClinikoApiKey(String clinikoApiKey) {
		this.clinikoApiKey = clinikoApiKey;
	}

	@DynamoDBAttribute(attributeName = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "Sync-Index", attributeName = "syncStatus")
	public Integer getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
	}

	@DynamoDBIndexRangeKey(globalSecondaryIndexName = "Sync-Index", attributeName = "timeStamp")
	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public SbmSyncFutureBookings(String sbmId, String clinikoApiKey, String email, Integer syncStatus, Long timeStamp) {

		this.sbmId = sbmId;
		this.clinikoApiKey = clinikoApiKey;
		this.email = email;
		this.syncStatus = syncStatus;
		this.timeStamp = timeStamp;
	}

	public SbmSyncFutureBookings(String sbmId, String clinikoApiKey, Integer syncStatus, Long timeStamp) {

		this.sbmId = sbmId;
		this.clinikoApiKey = clinikoApiKey;
		this.syncStatus = syncStatus;
		this.timeStamp = timeStamp;
	}

	public SbmSyncFutureBookings() {

	}

}
