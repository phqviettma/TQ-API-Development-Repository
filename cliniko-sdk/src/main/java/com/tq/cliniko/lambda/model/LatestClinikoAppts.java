package com.tq.cliniko.lambda.model;

import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="LatestClinikoAppts")
public class LatestClinikoAppts {
	public static final String LATEST_UPDATED_KEY = "latest_update";
	private String latest_update = LATEST_UPDATED_KEY;
	private Set<Long> created = null;
	private Set<Long> removed = null;
	
	
	@DynamoDBHashKey(attributeName = "latest_update")
	public String getLatest_update() {
		return latest_update;
	}
	public void setLatest_update(String latest_update) {
		this.latest_update = latest_update;
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
}
