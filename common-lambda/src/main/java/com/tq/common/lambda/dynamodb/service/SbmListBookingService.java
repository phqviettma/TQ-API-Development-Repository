package com.tq.common.lambda.dynamodb.service;

import com.tq.common.lambda.dynamodb.model.SbmBookingList;

public interface SbmListBookingService extends BaseItemService<SbmBookingList, String> {
	public void delete(SbmBookingList item);
}
