package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.GoogleCalendarDao;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;

public class GoogleCalendarServiceImpl implements GoogleCalendarDbService {
	private GoogleCalendarDao m_calendarSbmDao;

	public GoogleCalendarServiceImpl(GoogleCalendarDao calendarSbmDao) {
		m_calendarSbmDao = calendarSbmDao;
	}

	@Override
	public void put(GoogleCalendarSbmSync item) {
		m_calendarSbmDao.putItem(item);
	}

	@Override
	public GoogleCalendarSbmSync load(String key) {

		return m_calendarSbmDao.loadItem(key);
	}

	@Override
	public GoogleCalendarSbmSync query(String email) {

		return m_calendarSbmDao.queryIndex(email);
	}
}
