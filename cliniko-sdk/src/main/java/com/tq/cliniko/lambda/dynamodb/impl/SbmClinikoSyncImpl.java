package com.tq.cliniko.lambda.dynamodb.impl;

import com.tq.cliniko.lambda.dynamodb.dao.SbmClinikoSyncDao;
import com.tq.cliniko.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.cliniko.lambda.model.SbmCliniko;

public class SbmClinikoSyncImpl implements SbmClinikoSyncService {
	private SbmClinikoSyncDao m_SbmClinikoDao;

	 public SbmClinikoSyncImpl(SbmClinikoSyncDao sbmClinikoDao) {
		m_SbmClinikoDao = sbmClinikoDao;
	
	}
	@Override
	public void put(SbmCliniko item) {
	m_SbmClinikoDao.putItem(item);
	
	}

	@Override
	public SbmCliniko load(Integer sbmId) {
		
		return m_SbmClinikoDao.loadItem(sbmId);
	}

}
