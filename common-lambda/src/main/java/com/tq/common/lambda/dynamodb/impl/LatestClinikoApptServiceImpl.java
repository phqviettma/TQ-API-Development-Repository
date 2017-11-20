package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.LatestClinikoApptsDao;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppts;
import com.tq.common.lambda.dynamodb.service.LatestClinikoApptService;

public class LatestClinikoApptServiceImpl implements LatestClinikoApptService {

	private LatestClinikoApptsDao m_latestClinikoDao;

	public LatestClinikoApptServiceImpl(LatestClinikoApptsDao latestClinikoApptDao) {
		m_latestClinikoDao = latestClinikoApptDao;
	}

	@Override
	public void put(LatestClinikoAppts latestCliniko) {
		m_latestClinikoDao.putItem(latestCliniko);

	}

	@Override
	public LatestClinikoAppts load(String key) {
		return m_latestClinikoDao.loadItem(key);
	}
	
	
	

	
}
