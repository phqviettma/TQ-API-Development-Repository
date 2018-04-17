package com.tq.common.lambda.dynamodb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.service.AbstractItem;


public class SbmClinikoSyncDaoImpl extends AbstractItem<SbmCliniko, Long> implements SbmClinikoSyncDao {

	public SbmClinikoSyncDaoImpl(AmazonDynamoDB client) {
		super(client, SbmCliniko.class);
		
	}
	
	@Override
	public SbmCliniko loadItem(Long sbmId)
	{
		return super.loadItem(sbmId);
	}

	@Override
	public SbmCliniko queryIndex(Long clinikoId) {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":id", new AttributeValue().withN(clinikoId+""));
		DynamoDBQueryExpression<SbmCliniko> queryExpression = new DynamoDBQueryExpression<SbmCliniko>()
				.withIndexName("SbmCliniko-Index").withKeyConditionExpression("clinikoId=:id")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);

		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<SbmCliniko> sbmCliniko = mapper.query(SbmCliniko.class, queryExpression);

		if (sbmCliniko.size() > 0) {
			return sbmCliniko.get(0);
		} else {
			return null;

		}
	}

}
