package com.tq.common.lambda.dynamodb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class GoogleCalendarDaoImpl extends AbstractItem<GoogleCalendarSbmSync, String> implements GoogleCalendarDao {

	public GoogleCalendarDaoImpl(AmazonDynamoDB client) {
		super(client, GoogleCalendarSbmSync.class);

	}

	public GoogleCalendarSbmSync loadItem(String sbmId) {
		return super.loadItem(sbmId);
	}

	public List<GoogleCalendarSbmSync> queryEmail(String email) {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":email", new AttributeValue().withS(email));

		DynamoDBQueryExpression<GoogleCalendarSbmSync> queryExpression = new DynamoDBQueryExpression<GoogleCalendarSbmSync>()
				.withIndexName("Email-Index").withKeyConditionExpression("email=:email")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<GoogleCalendarSbmSync> calendarSbmSync = mapper.query(GoogleCalendarSbmSync.class, queryExpression);

		return calendarSbmSync;
	}

	@Override
	public void deleteGoogleItem(List<GoogleCalendarSbmSync> googleItem) {
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		mapper.batchDelete(googleItem);

	}

}
