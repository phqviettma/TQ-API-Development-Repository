package com.tq.common.lambda.dynamodb.impl;

import java.util.List;

import com.tq.common.lambda.dynamodb.dao.ClinikoItemDao;
import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.service.ClinikoItemService;

public class ClinikoItemServiceImpl implements ClinikoItemService {
	private ClinikoItemDao clinikoDao;

	public ClinikoItemServiceImpl(ClinikoItemDao clinikoDao) {
		this.clinikoDao = clinikoDao;
	}

	@Override
	public void put(ClinikoSyncStatus item) {
		clinikoDao.putItem(item);

	}

	@Override
	public ClinikoSyncStatus load(String key) {

		return clinikoDao.loadItem(key);
	}

	@Override
	public void delete(ClinikoSyncStatus clinikoItem) {
		clinikoDao.deleteItem(clinikoItem);

	}

	@Override
	public List<ClinikoSyncStatus> queryIndex() {

		return clinikoDao.queryIndex();
	}

	@Override
	public ClinikoSyncStatus queryWithIndex(String apiKey) {
		return clinikoDao.queryIndex(apiKey);
	}

}
