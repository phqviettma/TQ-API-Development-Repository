package com.tq.common.lambda.dynamodb.dao;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.service.GenericItem;

public interface GoogleCalendarModifiedSyncDao extends GenericItem<GCModifiedChannel, String> {
	public List<GCModifiedChannel> scanItem();

	public List<GCModifiedChannel> queryIndexCheckStatus();

	void saveItem(GCModifiedChannel modifiedChannel);

	public List<GCModifiedChannel> queryEmail(String email);

	public void deleteModifiedItem(List<GCModifiedChannel> gcModified);

	public GCModifiedChannel getChannel(String hashKey, String rangeKey);
}
