package com.tq.common.lambda.dynamodb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tq.common.lambda.dynamodb.model.CountryItem;
import com.tq.common.lambda.dynamodb.service.AbstractItem;
import com.tq.common.lambda.utils.Utils;

public class CountryItemDaoImpl extends AbstractItem<CountryItem, String> implements CountryItemDao {

	public CountryItemDaoImpl(AmazonDynamoDB client) {
		super(client, CountryItem.class);
	}

	@Override
	public CountryItem loadItem(String countryName) {
		if (Utils.isEmpty(countryName))
			return null;
		return super.loadItem(countryName);
	}

	@Override
	public String queryCountryCode(String code) {
		Map<String, AttributeValue> queryCondition = new HashMap<String, AttributeValue>();
		queryCondition.put(":code", new AttributeValue().withS(code));
		DynamoDBQueryExpression<CountryItem> queryExpression = new DynamoDBQueryExpression<CountryItem>()
				.withIndexName("Country-Index").withKeyConditionExpression("code=:code")
				.withExpressionAttributeValues(queryCondition).withConsistentRead(false);

		DynamoDBMapper mapper = new DynamoDBMapper(getClient());
		List<CountryItem> countryItem = mapper.query(CountryItem.class, queryExpression);

		if (countryItem.size() > 0) {
			return countryItem.get(0).getName();
		} else {
			return null;

		}

	}

}
