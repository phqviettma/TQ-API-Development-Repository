package com.tq.cliniko.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.cliniko.lambda.dynamodb.service.AbstractItem;
import com.tq.cliniko.lambda.model.SbmCliniko;


public class SbmClinikoSyncDaoImpl extends AbstractItem<SbmCliniko, Integer> implements SbmClinikoSyncDao {

	public SbmClinikoSyncDaoImpl(AmazonDynamoDB client) {
		super(client, SbmCliniko.class);
		
	}
	@Override
	public SbmCliniko loadItem(Integer sbmId)
	{
		return super.loadItem(sbmId);
	}

}
