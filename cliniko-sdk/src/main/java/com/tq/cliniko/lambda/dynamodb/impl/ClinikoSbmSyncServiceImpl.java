package com.tq.cliniko.lambda.dynamodb.impl;

import com.tq.cliniko.lambda.dynamodb.dao.ClinikoSbmSyncDao;
import com.tq.cliniko.lambda.dynamodb.service.ClinikoSbmSyncService;
import com.tq.cliniko.lambda.model.ClinikoSbm;

public class ClinikoSbmSyncServiceImpl implements ClinikoSbmSyncService {
	private ClinikoSbmSyncDao m_clinikoSbmDao;

	public ClinikoSbmSyncServiceImpl(ClinikoSbmSyncDao clinikoSbmDao) {
		m_clinikoSbmDao = clinikoSbmDao;
	}

	@Override
	public void put(ClinikoSbm item) {
		m_clinikoSbmDao.putItem(item);
	}

	@Override
	public ClinikoSbm load(Integer clinikoId) {

		return m_clinikoSbmDao.loadItem(clinikoId);
	}

}
