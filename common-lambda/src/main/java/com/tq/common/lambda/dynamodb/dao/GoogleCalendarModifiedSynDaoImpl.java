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
	public List<GCModifiedChannel> queryItem() {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":checkStatus", new AttributeValue().withN("1"));
		DynamoDBQueryExpression<GCModifiedChannel> queryExpression = new DynamoDBQueryExpression<GCModifiedChannel>()
				.withIndexName("Modified-Index").withKeyConditionExpression("checkStatus=:checkStatus")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);

		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<GCModifiedChannel> calendarSbmSync = mapper.query(GCModifiedChannel.class, queryExpression);
		return calendarSbmSync;
	}

	@Override
	public void deleteItem(String hashKey) {
		GCModifiedChannel channel = new GCModifiedChannel();
		channel.setChannelId(hashKey);
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		mapper.delete(channel);
	}

	@Override
	public void saveItem(GCModifiedChannel item) {
		super.saveItem(item);
	}
	

}
