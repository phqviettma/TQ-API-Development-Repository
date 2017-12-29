package com.tq.common.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.dynamodb.model.CalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class CalendarSbmDaoImpl extends AbstractItem<CalendarSbmSync, String> implements CalendarSbmDao {

	public CalendarSbmDaoImpl(AmazonDynamoDB client) {
		super(client, CalendarSbmSync.class);

	}

	public CalendarSbmSync loadItem(String sbmId) {
		return super.loadItem(sbmId);
	}

}
