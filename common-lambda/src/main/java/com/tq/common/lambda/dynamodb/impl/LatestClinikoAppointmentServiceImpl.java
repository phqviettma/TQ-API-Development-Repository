package com.tq.common.lambda.dynamodb.impl;

import java.util.List;

import com.tq.common.lambda.dynamodb.dao.LatestClinikoAppointmentDao;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppointment;
import com.tq.common.lambda.dynamodb.service.LatestClinikoAppointmentService;

public class LatestClinikoAppointmentServiceImpl implements LatestClinikoAppointmentService {

	private LatestClinikoAppointmentDao m_latestClinikoDao;

	public LatestClinikoAppointmentServiceImpl(LatestClinikoAppointmentDao latestClinikoApptDao) {
		m_latestClinikoDao = latestClinikoApptDao;
	}

	@Override
	public void put(LatestClinikoAppointment item) {
		m_latestClinikoDao.putItem(item);

	}

	@Override
	public LatestClinikoAppointment load(String key) {

		return m_latestClinikoDao.loadItem(key);
	}

	@Override
	public void delete(LatestClinikoAppointment clinikoAppt) {
		m_latestClinikoDao.deleteItem(clinikoAppt);

	}

	@Override
	public List<LatestClinikoAppointment> queryItem() {
		return m_latestClinikoDao.queryItem();

	}

}
