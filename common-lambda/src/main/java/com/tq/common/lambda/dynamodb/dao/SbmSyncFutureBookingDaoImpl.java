package com.tq.common.lambda.dynamodb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class SbmSyncFutureBookingDaoImpl extends AbstractItem<SbmSyncFutureBookings, String> implements SbmSyncFutureBookingDao{

	public SbmSyncFutureBookingDaoImpl(AmazonDynamoDB client) {
		super(client, SbmSyncFutureBookings.class);

	}

	@Override
	public List<SbmSyncFutureBookings> querySyncStatus() {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":syncStatus", new AttributeValue().withN("1"));
		DynamoDBQueryExpression<SbmSyncFutureBookings> queryExpression = new DynamoDBQueryExpression<SbmSyncFutureBookings>()
				.withIndexName("Sync-Index").withKeyConditionExpression("syncStatus=:syncStatus")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);

		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<SbmSyncFutureBookings> sbmSync = mapper.query(SbmSyncFutureBookings.class, queryExpression);
		return sbmSync;
	}

}
