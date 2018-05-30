package com.tq.common.lambda.dynamodb.service;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;

public interface SbmSyncFutureBookingsService extends BaseItemService<SbmSyncFutureBookings, String> {
	public List<SbmSyncFutureBookings> querySyncStatus();
	public void delete(SbmSyncFutureBookings sbmSyncFutureBookings);
}
