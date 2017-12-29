package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.CalendarSbmDao;
import com.tq.common.lambda.dynamodb.model.CalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.CalendarSbmService;

public class CalendarSbmServiceImpl implements CalendarSbmService {
	private CalendarSbmDao m_calendarSbmDao;

	public CalendarSbmServiceImpl(CalendarSbmDao calendarSbmDao) {
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
