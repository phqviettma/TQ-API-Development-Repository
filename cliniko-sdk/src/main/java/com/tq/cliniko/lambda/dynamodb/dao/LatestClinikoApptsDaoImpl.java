package com.tq.cliniko.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.cliniko.lambda.model.LatestClinikoAppts;
import com.tq.common.lambda.dynamodb.service.AbstractItem;


public class LatestClinikoApptsDaoImpl extends AbstractItem<LatestClinikoAppts, String> implements LatestClinikoApptsDao {

	public LatestClinikoApptsDaoImpl(AmazonDynamoDB client) {
		super(client, LatestClinikoAppts.class);
		
	}
	
	@Override
	public LatestClinikoAppts loadItem(String id) {
		return super.loadItem(id);
	}

}
