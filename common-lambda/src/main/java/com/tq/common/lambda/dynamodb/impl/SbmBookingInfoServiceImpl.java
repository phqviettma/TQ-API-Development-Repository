package com.tq.common.lambda.dynamodb.impl;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.tq.common.lambda.dynamodb.dao.SbmBookingInfoDao;
import com.tq.common.lambda.dynamodb.model.SbmBookingInfo;
import com.tq.common.lambda.dynamodb.service.SbmBookingInfoService;

public class SbmBookingInfoServiceImpl implements SbmBookingInfoService {
	private SbmBookingInfoDao sbmBookingInfoDao;

	public SbmBookingInfoServiceImpl(SbmBookingInfoDao sbmBookingInfoDao) {
		this.sbmBookingInfoDao = sbmBookingInfoDao;
	}

	@Override
	public void put(SbmBookingInfo item) {
		sbmBookingInfoDao.putItem(item);
	}

	@Override
	public SbmBookingInfo load(Long key) {

		return sbmBookingInfoDao.loadItem(key);
	}

	@Override
	public void delete(SbmBookingInfo item) {
		sbmBookingInfoDao.deleteItem(item);

	}

	@Override
	public QueryResultPage<SbmBookingInfo> getListBooking(String email, int size) {

		return sbmBookingInfoDao.getListBooking(email, size);
	}

	@Override
	public List<SbmBookingInfo> queryEmailIndex(String email) {

		return sbmBookingInfoDao.queryEmailIndex(email);
	}

	@Override
	public void deleteListBookings(List<SbmBookingInfo> sbmBookingInfo) {

		sbmBookingInfoDao.deleteListBookingItem(sbmBookingInfo);

	}

	@Override
	public QueryResultPage<SbmBookingInfo> queryClientEmail(Integer size, String clientEmail) {

		return sbmBookingInfoDao.queryClientEmail(size, clientEmail);
	}

}
