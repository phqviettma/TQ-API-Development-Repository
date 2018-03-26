package com.tq.common.lambda.dynamodb.service;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;

public interface CalendarSyncService extends BaseItemService<GCModifiedChannel, String> {
	public List<GCModifiedChannel> scanItem();

	public void delete(GCModifiedChannel modifiedChannel);
}
