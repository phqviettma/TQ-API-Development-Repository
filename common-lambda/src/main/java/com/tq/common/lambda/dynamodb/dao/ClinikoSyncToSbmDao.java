package com.tq.common.lambda.dynamodb.dao;

import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.service.GenericItem;

public interface ClinikoSyncToSbmDao extends GenericItem<ClinikoSbmSync, String> {
	public ClinikoSbmSync queryIndex(String apiKey);

	public ClinikoSbmSync queryEmail(String practitionerEmail);
}
