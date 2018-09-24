package com.tq.common.lambda.dynamodb.dao;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.tq.common.lambda.dynamodb.model.SbmBookingInfo;
import com.tq.common.lambda.dynamodb.service.GenericItem;

public interface SbmBookingInfoDao extends GenericItem<SbmBookingInfo, Long> {
	public QueryResultPage<SbmBookingInfo> getListBooking(String email, int size);

	public List<SbmBookingInfo> queryEmailIndex(String email);

	public void deleteListBookingItem(List<SbmBookingInfo> sbmBookingsInfo);
	
	public QueryResultPage<SbmBookingInfo> queryClientEmail(Integer size, String clientEmail);

}
