package com.tq.common.lambda.dynamodb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class ClinikoSyncToSbmDaoImpl extends AbstractItem<ClinikoSbmSync, String> implements ClinikoSyncToSbmDao {

	public ClinikoSyncToSbmDaoImpl(AmazonDynamoDB client) {
		super(client, ClinikoSbmSync.class);

	}

	@Override
	public ClinikoSbmSync loadItem(String apiKey) {
		return super.loadItem(apiKey);
	}

	@Override
	public ClinikoSbmSync queryIndex(String apiKey) {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":apiKey", new AttributeValue().withS(apiKey));
		DynamoDBQueryExpression<ClinikoSbmSync> queryExpression = new DynamoDBQueryExpression<ClinikoSbmSync>()
				.withIndexName("ClinikoSbm-Index").withKeyConditionExpression("apiKey=:apiKey")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<ClinikoSbmSync> clinikoSbm = mapper.query(ClinikoSbmSync.class, queryExpression);

		if (clinikoSbm.size() > 0) {
			return clinikoSbm.get(0);
		} else {
			return null;

		}
	}

	@Override
	public ClinikoSbmSync queryEmail(String practitionerEmail) {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":practitionerEmail", new AttributeValue().withS(practitionerEmail));
		DynamoDBQueryExpression<ClinikoSbmSync> queryExpression = new DynamoDBQueryExpression<ClinikoSbmSync>()
				.withIndexName("Email-Index").withKeyConditionExpression("email=:practitionerEmail")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<ClinikoSbmSync> clinikoSbm = mapper.query(ClinikoSbmSync.class, queryExpression);

		if (clinikoSbm.size() > 0) {
			return clinikoSbm.get(0);
		} else {
			return null;

		}
	}

}
