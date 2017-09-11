package com.tq.cliniko.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.cliniko.lambda.model.ClinikoSbm;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class ClinikoSbmSyncDaoImpl extends AbstractItem<ClinikoSbm, Integer> implements ClinikoSbmSyncDao {

	public ClinikoSbmSyncDaoImpl(AmazonDynamoDB client) {
		super(client, ClinikoSbm.class);
	}

	@Override
	public ClinikoSbm loadItem(Integer clinikoId) {
	
		return super.loadItem(clinikoId);

	}

}
