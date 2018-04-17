package com.tq.common.lambda.dynamodb.service;

import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;

public interface ClinikoSyncToSbmService extends BaseItemService<ClinikoSbmSync, String> {
	public void delete(ClinikoSbmSync clinikoSbm);

	public ClinikoSbmSync queryWithIndex(String apiKey);
}
