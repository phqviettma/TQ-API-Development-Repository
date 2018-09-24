package com.tq.common.lambda.dynamodb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tq.common.lambda.dynamodb.model.SbmBookingInfo;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class SbmBookingInfoDaoImpl extends AbstractItem<SbmBookingInfo, Long> implements SbmBookingInfoDao {
	public SbmBookingInfoDaoImpl(AmazonDynamoDB client) {
		super(client, SbmBookingInfo.class);
	}

	@Override
	public QueryResultPage<SbmBookingInfo> getListBooking(String email, int size) {
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
		expressionAttributeValues.put(":email", new AttributeValue().withS(email));
		DynamoDBQueryExpression<SbmBookingInfo> queryExpression = new DynamoDBQueryExpression<SbmBookingInfo>()
				.withIndexName("practitioner-index").withKeyConditionExpression("email=:email")
				.withExpressionAttributeValues(expressionAttributeValues).withConsistentRead(false).withLimit(size)
				.withScanIndexForward(false);
		QueryResultPage<SbmBookingInfo> result = mapper.queryPage(SbmBookingInfo.class, queryExpression);
		return result;

	}

	@Override
	public List<SbmBookingInfo> queryEmailIndex(String email) {

		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":email", new AttributeValue().withS(email));
		DynamoDBQueryExpression<SbmBookingInfo> queryExpression = new DynamoDBQueryExpression<SbmBookingInfo>()
				.withIndexName("practitioner-index").withKeyConditionExpression("email=:email")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);

		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<SbmBookingInfo> sbmBookingLists = mapper.query(SbmBookingInfo.class, queryExpression);
		return sbmBookingLists;

	}

	@Override
	public void deleteListBookingItem(List<SbmBookingInfo> sbmBookingsInfo) {
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		mapper.batchDelete(sbmBookingsInfo);

	}

	@Override
	public QueryResultPage<SbmBookingInfo> queryClientEmail(Integer size , String clientEmail) {
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
		expressionAttributeValues.put(":clientEmail", new AttributeValue().withS(clientEmail));
		DynamoDBQueryExpression<SbmBookingInfo> queryExpression = new DynamoDBQueryExpression<SbmBookingInfo>()
				.withIndexName("Client-Index").withKeyConditionExpression("clientEmail=:clientEmail")
				.withExpressionAttributeValues(expressionAttributeValues).withConsistentRead(false).withLimit(size)
				.withScanIndexForward(false);
		QueryResultPage<SbmBookingInfo> result = mapper.queryPage(SbmBookingInfo.class, queryExpression);
		return result;

	}

}
