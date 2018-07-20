package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.SbmListBookingDao;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;

public class SbmListBookingServiceImpl implements SbmListBookingService {

	private SbmListBookingDao sbmListBookingDao;

	public SbmListBookingServiceImpl(SbmListBookingDao sbmBookingDao) {
		sbmListBookingDao = sbmBookingDao;
	}

	@Override
	public void put(SbmBookingList item) {
		sbmListBookingDao.putItem(item);

	}

	@Override
	public SbmBookingList load(String key) {

		return sbmListBookingDao.loadItem(key);
	}

	@Override
	public void delete(SbmBookingList item) {
		sbmListBookingDao.deleteItem(item);

	}

}
