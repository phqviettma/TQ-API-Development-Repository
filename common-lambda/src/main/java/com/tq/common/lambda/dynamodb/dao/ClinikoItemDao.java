package com.tq.common.lambda.dynamodb.dao;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.service.GenericItem;

public interface ClinikoItemDao extends GenericItem<ClinikoSyncStatus, String> {

	public List<ClinikoSyncStatus> queryIndex();
	
	public ClinikoSyncStatus queryIndex(String apiKey);
}
