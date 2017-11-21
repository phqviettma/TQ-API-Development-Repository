package com.tq.common.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppts;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class LatestClinikoApptsImpl extends AbstractItem<LatestClinikoAppts, String> implements LatestClinikoApptsDao{

	public LatestClinikoApptsImpl(AmazonDynamoDB client) {
		super(client, LatestClinikoAppts.class);
		
	}
	@Override
	public LatestClinikoAppts loadItem(String hashKey) {
		
		return super.loadItem(hashKey);
	}
}
