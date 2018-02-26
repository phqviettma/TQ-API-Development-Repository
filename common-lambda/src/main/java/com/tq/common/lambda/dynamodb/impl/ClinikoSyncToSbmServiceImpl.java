package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.ClinikoSyncToSbmDao;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;

public class ClinikoSyncToSbmServiceImpl implements ClinikoSyncToSbmService {
	private ClinikoSyncToSbmDao m_clinikoSbmDao;

	@Override
	public void put(ClinikoSbmSync item) {
		m_clinikoSbmDao.putItem(item);

	}

	@Override
	public ClinikoSbmSync load(String key) {
		return m_clinikoSbmDao.loadItem(key);
	}

	public ClinikoSyncToSbmServiceImpl(ClinikoSyncToSbmDao clinikoSbmDao) {
		m_clinikoSbmDao = clinikoSbmDao;
	}

	@Override
	public void delete(ClinikoSbmSync clinikoSbm) {
		m_clinikoSbmDao.deleteItem(clinikoSbm);

	}

	@Override
	public ClinikoSbmSync queryWithIndex(String email) {

		return m_clinikoSbmDao.queryIndex(email);
	}

}
