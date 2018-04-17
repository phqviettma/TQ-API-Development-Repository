package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.ClinikoCompanyInfoDao;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;

public class ClinikoCompanyInfoServiceImpl implements ClinikoCompanyInfoService {

	private ClinikoCompanyInfoDao clinikoCompanyInfoDao;
	public ClinikoCompanyInfoServiceImpl(ClinikoCompanyInfoDao clinikoCompanyInfoDao) {
		this.clinikoCompanyInfoDao = clinikoCompanyInfoDao;
	}
	@Override
	public void put(ClinikoCompanyInfo item) {
		clinikoCompanyInfoDao.putItem(item);

	}

	@Override
	public ClinikoCompanyInfo load(Integer key) {

		return clinikoCompanyInfoDao.loadItem(key);
	}

}
