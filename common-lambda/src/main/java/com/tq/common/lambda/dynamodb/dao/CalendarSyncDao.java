package com.tq.common.lambda.dynamodb.dao;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.service.GenericItem;

public interface CalendarSyncDao extends GenericItem<GCModifiedChannel, String> {
	public List<GCModifiedChannel> scanItem();
}
