package com.tq.cliniko.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;



public class TestClinikoSbmSync {
	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
	static DynamoDB dynamoDB = new DynamoDB(client);

	public static void main(String[] args) {
		/*AmazonDynamoDB client = DynamodbUtils.getLocallyDynamoDB();
		ClinikoSbmSyncDao clinikoSbmSyncDao = new ClinikoSbmSyncDaoImpl(client);
		ClinikoSbmSyncService service = new ClinikoSbmSyncServiceImpl(clinikoSbmSyncDao);
		ClinikoSbm loadItem = service.load(2L);
		System.out.println("test load database ClinikoSbmSync: " + loadItem);
		SbmClinikoSyncDao sbmClinikoSyncDao = new SbmClinikoSyncDaoImpl(client);
		SbmClinikoSyncService sbmService = new SbmClinikoSyncImpl(sbmClinikoSyncDao);
		SbmCliniko loadSbmCliniko = sbmService.load(1L);
		System.out.println("test load database SbmClinikoSync: " + loadSbmCliniko);
		SbmCliniko item = new SbmCliniko();
		item.setClinikoId(2L);
		item.setSbmId(2L);
		sbmService.put(item);*/

	}
}
