package com.tq.common.lambda.dynamodb.dao;

import java.util.HashMap;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class CalendarSynDaoImpl extends AbstractItem<GCModifiedChannel, String> implements CalendarSyncDao {

	public CalendarSynDaoImpl(AmazonDynamoDB client) {
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
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("modified=:modified)").withExpressionAttributeValues(eav);

		List<GCModifiedChannel> modifiedChannel = mapper.scan(GCModifiedChannel.class, scanExpression);
		return modifiedChannel;
	}

}
