package com.tq.common.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.dynamodb.model.*;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class ClinikoSbmSyncDaoImpl extends AbstractItem<ClinikoSbm, Long> implements ClinikoSbmSyncDao {

	public ClinikoSbmSyncDaoImpl(AmazonDynamoDB client) {
		super(client, ClinikoSbm.class);
	}

	@Override
	public ClinikoSbm loadItem(Long clinikoId) {
	
		return super.loadItem(clinikoId);

	}
}
