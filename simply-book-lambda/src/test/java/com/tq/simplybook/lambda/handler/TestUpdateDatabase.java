package com.tq.simplybook.lambda.handler;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.dynamodb.dao.LatestClinikoApptsDao;
import com.tq.common.lambda.dynamodb.dao.LatestClinikoApptsImpl;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceImpl;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceWrapper;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppts;
import com.tq.common.lambda.dynamodb.service.LatestClinikoApptService;
import com.tq.common.lambda.utils.DynamodbUtils;

public class TestUpdateDatabase {
	private static AmazonDynamoDB client = DynamodbUtils.getLocallyDynamoDB();
	private static LatestClinikoApptsDao latestClinikoApptDao = new LatestClinikoApptsImpl(client);
	private static LatestClinikoApptService latestService = new LatestClinikoApptServiceImpl(latestClinikoApptDao);
	private static LatestClinikoApptServiceWrapper service = new LatestClinikoApptServiceWrapper(latestService);
	
	public static void main(String[] args) {
		LatestClinikoAppts la = service.load();
		Set<Long> created = la.getCreated();
		created.add(3L);
		la.setCreated(created);
		
		Date date = new Date();
		la.setLatest_update("latest_update");
		la.setLatestUpdateTime(Config.DATE_FORMAT_24_H.format(date));
		
		service.put(la);
		
	}
	
}
