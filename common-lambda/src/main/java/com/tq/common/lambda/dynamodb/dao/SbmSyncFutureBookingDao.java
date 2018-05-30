package com.tq.common.lambda.dynamodb.dao;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.GenericItem;

public interface SbmSyncFutureBookingDao extends GenericItem<SbmSyncFutureBookings, String> {
	public List<SbmSyncFutureBookings> querySyncStatus();
}
