package com.tq.common.lambda.dynamodb.dao;

import com.tq.common.lambda.dynamodb.model.CountryItem;
import com.tq.common.lambda.dynamodb.service.GenericItem;

public interface CountryItemDao extends GenericItem<CountryItem, String> {

	public String queryCountryCode(String countryCode);
}
