package com.tq.common.lambda.dynamodb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppointment;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class LatestClinikoAppointmentImpl extends AbstractItem<LatestClinikoAppointment, String>
		implements LatestClinikoAppointmentDao {

	public LatestClinikoAppointmentImpl(AmazonDynamoDB client) {
		super(client, LatestClinikoAppointment.class);
	}

	@Override
	public LatestClinikoAppointment loadItem(String hashKey) {

		return super.loadItem(hashKey);
	}

	@Override
	public List<LatestClinikoAppointment> queryItem() {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":checkFlag", new AttributeValue().withN("1"));
		DynamoDBQueryExpression<LatestClinikoAppointment> queryExpression = new DynamoDBQueryExpression<LatestClinikoAppointment>()
				.withIndexName("Cliniko-Index").withKeyConditionExpression("checkFlag=:checkFlag")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);

		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<LatestClinikoAppointment> calendarSbmSync = mapper.query(LatestClinikoAppointment.class, queryExpression);
		return calendarSbmSync;
	}

}
