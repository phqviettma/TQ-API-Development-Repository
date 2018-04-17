package com.tq.common.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class ClinikoCompanyInfoDaoImpl extends AbstractItem<ClinikoCompanyInfo, Integer>
		implements ClinikoCompanyInfoDao {

	public ClinikoCompanyInfoDaoImpl(AmazonDynamoDB client) {
		super(client, ClinikoCompanyInfo.class);

	}

}
