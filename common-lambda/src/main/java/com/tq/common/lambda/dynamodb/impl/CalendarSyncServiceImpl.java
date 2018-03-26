package com.tq.common.lambda.dynamodb.impl;

import java.util.List;

import com.tq.common.lambda.dynamodb.dao.CalendarSyncDao;
import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.service.CalendarSyncService;

public class CalendarSyncServiceImpl implements CalendarSyncService {
	private CalendarSyncDao calendarSyncDao;

	public CalendarSyncServiceImpl(CalendarSyncDao calendarDao) {
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

}
