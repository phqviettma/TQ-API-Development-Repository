package com.tq.cliniko.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.tq.cliniko.lambda.dynamodb.dao.ClinikoSbmSyncDao;
import com.tq.cliniko.lambda.dynamodb.dao.ClinikoSbmSyncDaoImpl;
import com.tq.cliniko.lambda.dynamodb.dao.SbmClinikoSyncDao;
import com.tq.cliniko.lambda.dynamodb.dao.SbmClinikoSyncDaoImpl;
import com.tq.cliniko.lambda.dynamodb.impl.ClinikoSbmSyncServiceImpl;
import com.tq.cliniko.lambda.dynamodb.impl.SbmClinikoSyncImpl;
import com.tq.cliniko.lambda.dynamodb.service.ClinikoSbmSyncService;
import com.tq.cliniko.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.cliniko.lambda.model.ClinikoSbm;
import com.tq.cliniko.lambda.model.SbmCliniko;
import com.tq.common.lambda.utils.DynamodbUtils;


public class TestClinikoSbmSync {
	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
	static DynamoDB dynamoDB = new DynamoDB(client);

	public static void main(String[] args) {
		AmazonDynamoDB client = DynamodbUtils.getLocallyDynamoDB();
		ClinikoSbmSyncDao clinikoSbmSyncDao = new ClinikoSbmSyncDaoImpl(client);
		ClinikoSbmSyncService service = new ClinikoSbmSyncServiceImpl(clinikoSbmSyncDao);
		ClinikoSbm loadItem = service.load(2);
		System.out.println("test load database ClinikoSbmSync: " + loadItem);
		SbmClinikoSyncDao sbmClinikoSyncDao = new SbmClinikoSyncDaoImpl(client);
		SbmClinikoSyncService sbmService = new SbmClinikoSyncImpl(sbmClinikoSyncDao);
		SbmCliniko loadSbmCliniko = sbmService.load(1);
		System.out.println("test load database SbmClinikoSync: " + loadSbmCliniko);
		SbmCliniko item = new SbmCliniko();
		item.setClinikoId(2);
		item.setSbmId(2);
		sbmService.put(item);

	}
}
