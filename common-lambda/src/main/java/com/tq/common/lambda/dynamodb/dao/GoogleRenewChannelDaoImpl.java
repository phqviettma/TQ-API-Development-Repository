package com.tq.common.lambda.dynamodb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class GoogleRenewChannelDaoImpl extends AbstractItem<GoogleRenewChannelInfo, String>
		implements GoogleRenewChannelDao {
	public GoogleRenewChannelDaoImpl(AmazonDynamoDB client) {
		super(client, GoogleRenewChannelInfo.class);
	}

	@Override
	public GoogleRenewChannelInfo loadItem(String hashKey) {
		return super.loadItem(hashKey);
	}

	@Override
	public GoogleRenewChannelInfo loadDbItem(Long hashKey, String rangeKey) {
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		GoogleRenewChannelInfo channelInfo = mapper.load(GoogleRenewChannelInfo.class, hashKey, rangeKey);
		return channelInfo;
	}

	@Override
	public List<GoogleRenewChannelInfo> queryCheckingTime(Long checkingTime) {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":checkDay", new AttributeValue().withN("" + checkingTime));
		DynamoDBQueryExpression<GoogleRenewChannelInfo> queryExpression = new DynamoDBQueryExpression<GoogleRenewChannelInfo>()
				.withIndexName("Time-Index").withKeyConditionExpression("checkDay=:checkDay")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<GoogleRenewChannelInfo> channelInfo = mapper.query(GoogleRenewChannelInfo.class, queryExpression);
		return channelInfo;
	}

	@Override
	public void saveItem(GoogleRenewChannelInfo item) {
		super.saveItem(item);
	}

	@Override
	public void deleteChannelList(List<GoogleRenewChannelInfo> googleChannelInfo) {
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		mapper.batchDelete(googleChannelInfo);

	}

	@Override
	public List<GoogleRenewChannelInfo> querySbmEmail(String sbmEmail) {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":googleEmail", new AttributeValue().withS(sbmEmail));
		DynamoDBQueryExpression<GoogleRenewChannelInfo> queryExpression = new DynamoDBQueryExpression<GoogleRenewChannelInfo>()
				.withIndexName("Email-Index").withKeyConditionExpression("googleEmail=:googleEmail")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<GoogleRenewChannelInfo> channelInfo = mapper.query(GoogleRenewChannelInfo.class, queryExpression);
		return channelInfo;
	}

}
