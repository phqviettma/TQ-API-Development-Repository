package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.ClinikoSbmSyncDao;
import com.tq.common.lambda.dynamodb.model.ClinikoSbm;
import com.tq.common.lambda.dynamodb.service.ClinikoSbmSyncService;

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
	public ClinikoSbm load(Long clinikoId) {

		return m_clinikoSbmDao.loadItem(clinikoId);
	}


}
