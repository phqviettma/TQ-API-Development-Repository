package com.tq.common.lambda.dynamodb.dao;

import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
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
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		LatestClinikoAppointment latestClinikoAppointment = new LatestClinikoAppointment();
		latestClinikoAppointment.setLatest_update(LatestClinikoAppointment.LATEST_UPDATED_KEY);
		DynamoDBQueryExpression<LatestClinikoAppointment> queryExpression = new DynamoDBQueryExpression<LatestClinikoAppointment>()
				.withHashKeyValues(latestClinikoAppointment);
		List<LatestClinikoAppointment> listItem = mapper.query(LatestClinikoAppointment.class, queryExpression);
		return listItem;
	}

}
