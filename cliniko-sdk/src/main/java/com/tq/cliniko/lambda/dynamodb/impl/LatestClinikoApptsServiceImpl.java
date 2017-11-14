package com.tq.cliniko.lambda.dynamodb.impl;

import com.tq.cliniko.lambda.dynamodb.dao.LatestClinikoApptsDao;
import com.tq.cliniko.lambda.dynamodb.service.LatestClinikoApptsService;
import com.tq.cliniko.lambda.model.LatestClinikoAppts;

public class LatestClinikoApptsServiceImpl implements LatestClinikoApptsService {
	private LatestClinikoApptsDao m_latestClinikoApptsDao;

	public LatestClinikoApptsServiceImpl(LatestClinikoApptsDao latestClinikoApptsDao) {
		m_latestClinikoApptsDao = latestClinikoApptsDao;
	}

	@Override
	public void put(LatestClinikoAppts item) {
		m_latestClinikoApptsDao.putItem(item);
	}

	@Override
	public LatestClinikoAppts load(String id) {
		return m_latestClinikoApptsDao.loadItem(id);
	}

}
