package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.SbmGoogleCalendarSyncDao;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;

public class SbmGoogleCalendarServiceImpl implements SbmGoogleCalendarDbService {
	private SbmGoogleCalendarSyncDao sbmGoogleCalendarDao;

	public SbmGoogleCalendarServiceImpl(SbmGoogleCalendarSyncDao sbmGCalendarDao) {
		sbmGoogleCalendarDao = sbmGCalendarDao;
	}

	@Override
	public void put(SbmGoogleCalendar item) {
		sbmGoogleCalendarDao.putItem(item);

	}

	@Override
	public SbmGoogleCalendar load(Long key) {

		return sbmGoogleCalendarDao.loadItem(key);
	}

	@Override
	public void delete(SbmGoogleCalendar sbmGoogleCalendarSync) {
		sbmGoogleCalendarDao.deleteItem(sbmGoogleCalendarSync);

	}

	@Override
	public SbmGoogleCalendar queryWithIndex(String eventId) {

		return sbmGoogleCalendarDao.queryIndex(eventId);
	}

}
