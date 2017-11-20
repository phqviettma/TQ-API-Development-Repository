package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.model.LatestClinikoAppts;
import com.tq.common.lambda.dynamodb.service.LatestClinikoApptService;

public class LatestClinikoApptServiceWrapper {
	private LatestClinikoApptService service;
	
	public LatestClinikoApptServiceWrapper(LatestClinikoApptService s) {
		service = s;
	}
	
	public LatestClinikoAppts load() {
		return service.load(LatestClinikoAppts.LATEST_UPDATED_KEY);
	}

	public void put(LatestClinikoAppts latestClinikoAppts) {
		service.put(latestClinikoAppts);
	}
	
}
