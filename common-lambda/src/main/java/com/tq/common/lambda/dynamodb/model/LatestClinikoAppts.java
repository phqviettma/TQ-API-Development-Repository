package com.tq.common.lambda.dynamodb.model;

import java.util.HashSet;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "LatestClinikoAppts")
public class LatestClinikoAppts {
	public static final String LATEST_UPDATED_KEY = "latest_update";
	private String latest_update = LATEST_UPDATED_KEY;
	private Set<Long> created = new HashSet<Long>();
	private Set<Long> removed = new HashSet<Long>();
	private String latest_update_time;
	
	public LatestClinikoAppts() {
		created.add(-1L);
		removed.add(-1L);
	}
	
	@DynamoDBAttribute(attributeName = "latest_update_time")
	public String getLatestUpdateTime() {
		return latest_update_time;
	}

	public void setLatestUpdateTime(String latestUpdateTime) {
		this.latest_update_time = latestUpdateTime;
	}

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

	@Override
	public String toString() {
		return "LatestClinikoAppts [latest_update=" + latest_update + ", created=" + created + ", removed=" + removed
				+ ", latest_update_time=" + latest_update_time + "]";
	}
	
	public static boolean isDefaultSet(Set<Long> set) {
		return set.size() > 1 || (set.size() == 1 && !set.contains(-1L));
	}
	
}
