package com.tq.common.lambda.dynamodb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class GoogleCalendarModifiedSynDaoImpl extends AbstractItem<GCModifiedChannel, String>
		implements GoogleCalendarModifiedSyncDao {
	public GoogleCalendarModifiedSynDaoImpl(AmazonDynamoDB client) {
		super(client, GCModifiedChannel.class);

	}

	@Override
	public GCModifiedChannel loadItem(String hashKey) {
		return super.loadItem(hashKey);
	}

	@Override
	public List<GCModifiedChannel> scanItem() {
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		HashMap<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":modified", new AttributeValue().withBOOL(true));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("modified=:modified)")
				.withExpressionAttributeValues(eav);

		List<GCModifiedChannel> modifiedChannel = mapper.scan(GCModifiedChannel.class, scanExpression);
		return modifiedChannel;
	}

	@Override
	public List<GCModifiedChannel> queryIndexCheckStatus() {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":checkingStatus", new AttributeValue().withN("1"));
		DynamoDBQueryExpression<GCModifiedChannel> queryExpression = new DynamoDBQueryExpression<GCModifiedChannel>()
				.withIndexName("Status-Index").withKeyConditionExpression("checkingStatus=:checkingStatus")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);

		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<GCModifiedChannel> calendarSbmSync = mapper.query(GCModifiedChannel.class, queryExpression);
		return calendarSbmSync;
	}

	@Override
	public void saveItem(GCModifiedChannel item) {
		super.saveItem(item);
	}

	@Override
	public List<GCModifiedChannel> queryEmail(String email) {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":email", new AttributeValue().withS(email));
		DynamoDBQueryExpression<GCModifiedChannel> queryExpression = new DynamoDBQueryExpression<GCModifiedChannel>()
				.withIndexName("Email-Index").withKeyConditionExpression("email=:email")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);

		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<GCModifiedChannel> calendarSbmSync = mapper.query(GCModifiedChannel.class, queryExpression);
		return calendarSbmSync;
	}

	@Override
	public void deleteModifiedItem(List<GCModifiedChannel> gcModifiedChannel) {
	DynamoDBMapper mapper = new DynamoDBMapper(getClient());
	mapper.batchDelete(gcModifiedChannel);
		
	}

	@Override
	public GCModifiedChannel getChannel(String hashKey, String rangeKey) {
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		return	mapper.load(GCModifiedChannel.class, hashKey,rangeKey);
	}

}
