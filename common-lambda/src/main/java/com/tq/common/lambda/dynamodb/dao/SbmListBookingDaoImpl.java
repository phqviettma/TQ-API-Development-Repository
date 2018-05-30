package com.tq.common.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class SbmListBookingDaoImpl extends AbstractItem<SbmBookingList, String> implements SbmListBookingDao {

	public SbmListBookingDaoImpl(AmazonDynamoDB client) {
		super(client, SbmBookingList.class);
	}

}
