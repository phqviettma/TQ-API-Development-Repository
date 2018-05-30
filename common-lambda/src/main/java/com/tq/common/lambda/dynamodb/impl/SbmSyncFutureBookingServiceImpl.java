package com.tq.common.lambda.dynamodb.impl;

import java.util.List;

import com.tq.common.lambda.dynamodb.dao.SbmSyncFutureBookingDao;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;

public class SbmSyncFutureBookingServiceImpl implements SbmSyncFutureBookingsService {
	private SbmSyncFutureBookingDao sbmSyncDao;

	public SbmSyncFutureBookingServiceImpl(SbmSyncFutureBookingDao sbmSyncDao) {
		this.sbmSyncDao = sbmSyncDao;
	}

	@Override
	public void put(SbmSyncFutureBookings item) {
		sbmSyncDao.putItem(item);

	}

	@Override
	public SbmSyncFutureBookings load(String key) {

		return sbmSyncDao.loadItem(key);
	}

	@Override
	public List<SbmSyncFutureBookings> querySyncStatus() {

		return sbmSyncDao.querySyncStatus();
	}

	@Override
	public void delete(SbmSyncFutureBookings sbmSyncFutureBookings) {
		sbmSyncDao.deleteItem(sbmSyncFutureBookings);
		
	}

}
