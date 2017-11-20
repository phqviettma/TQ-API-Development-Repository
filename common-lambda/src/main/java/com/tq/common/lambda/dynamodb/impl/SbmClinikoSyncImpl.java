package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.SbmClinikoSyncDao;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;

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
	public SbmCliniko load(Long sbmId) {
		
		return m_SbmClinikoDao.loadItem(sbmId);
	}
	@Override
	public void delete(SbmCliniko sbmCliniko) {
	m_SbmClinikoDao.deleteItem(sbmCliniko);
		
	}
}
