package com.tq.common.lambda.dynamodb.service;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;

public interface ClinikoItemService extends BaseItemService<ClinikoSyncStatus, String> {

	public void delete(ClinikoSyncStatus clinikoItem);

	public List<ClinikoSyncStatus> queryIndex();
	
	public ClinikoSyncStatus queryWithIndex(String apiKey);
}
