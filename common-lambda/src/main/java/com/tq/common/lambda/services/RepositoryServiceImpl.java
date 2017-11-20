package com.tq.common.lambda.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.dynamodb.dao.ContactItemDaoImpl;
import com.tq.common.lambda.dynamodb.dao.CountryItemDaoImpl;
import com.tq.common.lambda.dynamodb.dao.LatestClinikoApptsImpl;
import com.tq.common.lambda.dynamodb.dao.OrderItemDaoimpl;
import com.tq.common.lambda.dynamodb.dao.ProductItemDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmClinikoSyncDaoImpl;
import com.tq.common.lambda.dynamodb.impl.ContactItemServiceImpl;
import com.tq.common.lambda.dynamodb.impl.CountryItemServiceImpl;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceImpl;
import com.tq.common.lambda.dynamodb.impl.OrderItemServiceImpl;
import com.tq.common.lambda.dynamodb.impl.ProductItemServiceImpl;
import com.tq.common.lambda.dynamodb.impl.SbmClinikoSyncImpl;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.common.lambda.dynamodb.service.LatestClinikoApptService;
import com.tq.common.lambda.dynamodb.service.OrderItemService;
import com.tq.common.lambda.dynamodb.service.ProductItemService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;

public class RepositoryServiceImpl implements RepositoryService {

	private AmazonDynamoDB m_client;

	public RepositoryServiceImpl(AmazonDynamoDB client) {
		this.m_client = client;
	}

	@Override
	public ContactItemService getContactItemService() {
		return new ContactItemServiceImpl(new ContactItemDaoImpl(m_client));
	}

	@Override
	public CountryItemService getCountryItemService() {
		return new CountryItemServiceImpl(new CountryItemDaoImpl(m_client));
	}

	@Override
	public OrderItemService getOrderItemService() {
		return new OrderItemServiceImpl(new OrderItemDaoimpl(m_client));
	}

	@Override
	public ProductItemService getProductItemService() {
		return new ProductItemServiceImpl(new ProductItemDaoImpl(m_client));
	}

	@Override
	public LatestClinikoApptService getClinikoApptService() {

		return new LatestClinikoApptServiceImpl(new LatestClinikoApptsImpl(m_client));
	}

	@Override
	public SbmClinikoSyncService getSimplybookService() {
		
		return new SbmClinikoSyncImpl(new SbmClinikoSyncDaoImpl(m_client));
	}

}
