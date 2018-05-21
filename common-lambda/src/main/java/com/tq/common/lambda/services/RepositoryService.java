package com.tq.common.lambda.services;

import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.common.lambda.dynamodb.service.OrderItemService;
import com.tq.common.lambda.dynamodb.service.ProductItemService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;

public interface RepositoryService {

	ContactItemService getContactItemService();

	CountryItemService getCountryItemService();

	OrderItemService getOrderItemService();

	ProductItemService getProductItemService();

	SbmClinikoSyncService getSimplybookService();
}
