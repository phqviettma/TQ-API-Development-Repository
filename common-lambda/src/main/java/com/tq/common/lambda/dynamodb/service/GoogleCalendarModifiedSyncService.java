package com.tq.common.lambda.dynamodb.service;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;

public interface GoogleCalendarModifiedSyncService extends BaseItemService<GCModifiedChannel, String> {
	public List<GCModifiedChannel> scanItem();

	public void delete(GCModifiedChannel modifiedChannel);

	public List<GCModifiedChannel> queryCheckStatusIndex();

	public void deleteDynamoItem(String hashKey);

	public void saveItem(GCModifiedChannel modifiedChannel);

	public List<GCModifiedChannel> queryEmail(String email);

	public void deleteModifiedItem(List<GCModifiedChannel> gcModified);

	public GCModifiedChannel getChannel(String hashKey, String rangeKey);
}
