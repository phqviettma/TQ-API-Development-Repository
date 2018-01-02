package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.GoogleCalendarDao;
import com.tq.common.lambda.dynamodb.model.CalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarService;

public class GoogleCalendarServiceImpl implements GoogleCalendarService {
	private GoogleCalendarDao m_calendarSbmDao;

	public GoogleCalendarServiceImpl(GoogleCalendarDao calendarSbmDao) {
		m_calendarSbmDao = calendarSbmDao;
	}

	@Override
	public void put(CalendarSbmSync item) {
		m_calendarSbmDao.putItem(item);
	}

	@Override
	public CalendarSbmSync load(String key) {

		return m_calendarSbmDao.loadItem(key);
	}

}
