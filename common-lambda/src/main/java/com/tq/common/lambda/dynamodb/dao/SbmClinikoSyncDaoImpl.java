package com.tq.common.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.dynamodb.model.*;
import com.tq.common.lambda.dynamodb.service.AbstractItem;


public class SbmClinikoSyncDaoImpl extends AbstractItem<SbmCliniko, Long> implements SbmClinikoSyncDao {

	public SbmClinikoSyncDaoImpl(AmazonDynamoDB client) {
		super(client, SbmCliniko.class);
		
	}
	
	@Override
	public SbmCliniko loadItem(Long sbmId)
	{
		return super.loadItem(sbmId);
	}

}
