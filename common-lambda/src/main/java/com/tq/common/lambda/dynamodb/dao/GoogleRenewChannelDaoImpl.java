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

public class GoogleRenewChannelDaoImpl extends AbstractItem<GoogleRenewChannelInfo, Long>
		implements GoogleRenewChannelDao {
	public GoogleRenewChannelDaoImpl(AmazonDynamoDB client) {
		super(client, GoogleRenewChannelInfo.class);
	}

	@Override
	public GoogleRenewChannelInfo loadItem(Long checkDay) {
		return super.loadItem(checkDay);
	}

	@Override
	public GoogleRenewChannelInfo loadDbItem(Long hashKey, String rangeKey) {
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		GoogleRenewChannelInfo channelInfo = mapper.load(GoogleRenewChannelInfo.class, hashKey, rangeKey);
		return channelInfo;
	}

	@Override
	public List<GoogleRenewChannelInfo> queryItem(Long hashKey) {
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		GoogleRenewChannelInfo channelInfo = new GoogleRenewChannelInfo();
		channelInfo.setCheckDay(hashKey);
		DynamoDBQueryExpression<GoogleRenewChannelInfo> queryExpression = new DynamoDBQueryExpression<GoogleRenewChannelInfo>()
				.withHashKeyValues(channelInfo);
		List<GoogleRenewChannelInfo> listItem = mapper.query(GoogleRenewChannelInfo.class, queryExpression);
		return listItem;
	}

	@Override
	public void saveItem(GoogleRenewChannelInfo item) {
		super.saveItem(item);
	}

	@Override
	public GoogleRenewChannelInfo queryChannelId(String resourceId) {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":resourceId", new AttributeValue().withS(resourceId));
		DynamoDBQueryExpression<GoogleRenewChannelInfo> queryExpression = new DynamoDBQueryExpression<GoogleRenewChannelInfo>()
				.withIndexName("Resource-Index").withKeyConditionExpression("resourceId=:resourceId")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<GoogleRenewChannelInfo> channelInfo = mapper.query(GoogleRenewChannelInfo.class, queryExpression);
		if (channelInfo.size() > 0) {
			return channelInfo.get(0);
		} else {
			return null;

		}
		
	}

	@Override
	public void deleteChannelList(List<GoogleRenewChannelInfo> googleChannelInfo) {
		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		mapper.batchDelete(googleChannelInfo);

	}

}
