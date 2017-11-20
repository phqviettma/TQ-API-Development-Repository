package com.tq.common.lambda.dynamodb.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;

public abstract class AbstractItem<Item, ID> implements GenericItem<Item, ID> {
	private AmazonDynamoDB client;
	private Class<Item> item;

	public AbstractItem(AmazonDynamoDB client, Class<Item> item) {
		this.client = client;
		this.item = item;
	}

	public AmazonDynamoDB getClient() {
		return client;
	}

	public void setClient(AmazonDynamoDB client) {
		this.client = client;
	}

	public Class<Item> getItem() {
		return item;
	}

	public void setItem(Class<Item> item) {
		this.item = item;
	}

	@Override
	public void putItem(Item item) {
		DynamoDBMapper dymapper = new DynamoDBMapper(getClient());
		dymapper.save(item);
	}

	@Override
	public Item loadItem(ID hashKey) {
		DynamoDBMapper dymapper = new DynamoDBMapper(getClient());
		return dymapper.load(item, hashKey);
	}

	@Override
	public void deleteItem(Item item) {
		DynamoDBMapper dymapper = new DynamoDBMapper(getClient());
		dymapper.delete(item);
	}

	public void updateItem(Item item) {
		DynamoDBMapper dbMapper = new DynamoDBMapper(getClient(), new DynamoDBMapperConfig(SaveBehavior.UPDATE));
		dbMapper.save(item);
	}
}
