package com.tq.common.lambda.dynamodb.impl;

import java.util.List;

import com.tq.common.lambda.dynamodb.dao.GoogleCalendarModifiedSyncDao;
import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;

public class GoogleCalendarModifiedSyncServiceImpl implements GoogleCalendarModifiedSyncService {
	private GoogleCalendarModifiedSyncDao calendarSyncDao;

	public GoogleCalendarModifiedSyncServiceImpl(GoogleCalendarModifiedSyncDao calendarDao) {
		calendarSyncDao = calendarDao;
	}

	@Override
	public void put(GCModifiedChannel item) {
		calendarSyncDao.putItem(item);

	}

	@Override
	public GCModifiedChannel load(String key) {

		return calendarSyncDao.loadItem(key);
	}

	@Override
	public List<GCModifiedChannel> scanItem() {

		return calendarSyncDao.scanItem();
	}

	@Override
	public void delete(GCModifiedChannel modifiedChannel) {
		calendarSyncDao.deleteItem(modifiedChannel);

	}

	@Override
	public List<GCModifiedChannel> queryItem() {

		return calendarSyncDao.queryItem();
	}

	@Override
	public void deleteDynamoItem(String hashKey) {
		calendarSyncDao.deleteItem(hashKey);

	}

	@Override
	public void saveItem(GCModifiedChannel modifiedChannel) {
		calendarSyncDao.saveItem(modifiedChannel);
		
	}
	

}
