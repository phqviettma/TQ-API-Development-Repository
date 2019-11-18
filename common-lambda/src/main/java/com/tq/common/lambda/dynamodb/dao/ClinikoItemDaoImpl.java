package com.tq.common.lambda.dynamodb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class ClinikoItemDaoImpl extends AbstractItem<ClinikoSyncStatus, String> implements ClinikoItemDao {
	public ClinikoItemDaoImpl(AmazonDynamoDB client) {
		super(client, ClinikoSyncStatus.class);

	}

	@Override
	public ClinikoSyncStatus loadItem(String hashKey) {

		return super.loadItem(hashKey);
	}

	@Override
	public List<ClinikoSyncStatus> queryIndex() {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":checkingItem", new AttributeValue().withS(ClinikoSyncStatus.CHECK_KEY));
		DynamoDBQueryExpression<ClinikoSyncStatus> queryExpression = new DynamoDBQueryExpression<ClinikoSyncStatus>()
				.withIndexName("Cliniko-Index").withKeyConditionExpression("checkingItem=:checkingItem")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false).withLimit(100);
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<ClinikoSyncStatus> clinikoItems = mapper.query(ClinikoSyncStatus.class, queryExpression);
		return clinikoItems;
	}

	@Override
	public ClinikoSyncStatus queryIndex(String apiKey) {
		List<ClinikoSyncStatus> clinikoItems = queryIndex();
		for (ClinikoSyncStatus item : clinikoItems) {
			if(apiKey.equals(item.getApiKey())) {
				return item;
			}
		}
		return null;
	}

}
