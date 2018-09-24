package com.tq.common.lambda.dynamodb.service;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.tq.common.lambda.dynamodb.model.SbmBookingInfo;

public interface SbmBookingInfoService extends BaseItemService<SbmBookingInfo, Long> {
	public void delete(SbmBookingInfo item);

	public QueryResultPage<SbmBookingInfo> getListBooking(String email, int size);

	public List<SbmBookingInfo> queryEmailIndex(String email);

	public void deleteListBookings(List<SbmBookingInfo> sbmBookingInfo);

	public QueryResultPage<SbmBookingInfo> queryClientEmail(Integer size, String clientEmail);
	
}
