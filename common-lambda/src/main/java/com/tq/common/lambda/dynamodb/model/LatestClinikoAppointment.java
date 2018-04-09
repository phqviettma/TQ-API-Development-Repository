package com.tq.common.lambda.dynamodb.model;

import java.util.HashSet;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "LatestClinikoAppointment")
public class LatestClinikoAppointment {
	private String apiKey;
	private Set<Long> created = new HashSet<Long>();
	private Set<Long> removed = new HashSet<Long>();
	private String latest_update_time;
	private Integer checkFlag;
	private Long timeStamp;

	public LatestClinikoAppointment() {
		created.add(-1L);
		removed.add(-1L);
	}

	@DynamoDBAttribute(attributeName = "created")
	public Set<Long> getCreated() {
		return created;
	}

	public void setCreated(Set<Long> created) {
		this.created = created;
	}

	@DynamoDBAttribute(attributeName = "removed")
	public Set<Long> getRemoved() {
		return removed;
	}

	public void setRemoved(Set<Long> removed) {
		this.removed = removed;
	}

	@DynamoDBAttribute(attributeName = "latest_update_time")
	public String getLatest_update_time() {
		return latest_update_time;
	}

	public void setLatest_update_time(String latest_update_time) {
		this.latest_update_time = latest_update_time;
	}

	@DynamoDBHashKey(attributeName = "apiKey")
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	@DynamoDBIndexHashKey(globalSecondaryIndexName="Cliniko-Index", attributeName = "checkFlag")
	public Integer getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(Integer checkFlag) {
		this.checkFlag = checkFlag;
	}
	@DynamoDBIndexRangeKey(globalSecondaryIndexName="Cliniko-Index",attributeName = "timeStamp")
	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "LatestClinikoAppointment [apiKey=" + apiKey + ", created=" + created + ", removed=" + removed
				+ ", latest_update_time=" + latest_update_time + ", checkFlag=" + checkFlag + "]";
	}

	public static boolean isDefaultSet(Set<Long> set) {
		return set.size() > 1 || (set.size() == 1 && !set.contains(-1L));
	}

	public LatestClinikoAppointment(Set<Long> created, Set<Long> removed, String latest_update_time, String apiKey) {
		this.created = created;
		this.removed = removed;
		this.latest_update_time = latest_update_time;
		this.apiKey = apiKey;
	}

	public LatestClinikoAppointment(String apiKey, String latest_update_time, Integer checkFlag, Long timeStamp) {
		this.apiKey = apiKey;
		this.latest_update_time = latest_update_time;
		this.checkFlag = checkFlag;
		this.timeStamp = timeStamp;
	}
	
	

}
