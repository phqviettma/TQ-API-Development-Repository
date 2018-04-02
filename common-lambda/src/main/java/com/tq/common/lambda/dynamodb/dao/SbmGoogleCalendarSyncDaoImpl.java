package com.tq.common.lambda.dynamodb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class SbmGoogleCalendarSyncDaoImpl extends AbstractItem<SbmGoogleCalendar, Long>
		implements SbmGoogleCalendarSyncDao {
	public SbmGoogleCalendarSyncDaoImpl(AmazonDynamoDB client) {
		super(client, SbmGoogleCalendar.class);

	}

	@Override
	public SbmGoogleCalendar loadItem(Long hashKey) {

		return super.loadItem(hashKey);
	}

	@Override
	public SbmGoogleCalendar queryIndex(String eventId) {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":id", new AttributeValue().withS(eventId));
		DynamoDBQueryExpression<SbmGoogleCalendar> queryExpression = new DynamoDBQueryExpression<SbmGoogleCalendar>()
				.withIndexName("Event-Index").withKeyConditionExpression("eventId=:id")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);

		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<SbmGoogleCalendar> sbmCalendarSync = mapper.query(SbmGoogleCalendar.class, queryExpression);

		if (sbmCalendarSync.size() > 0) {
			return sbmCalendarSync.get(0);
		} else {
			return null;

		}
	}

}
