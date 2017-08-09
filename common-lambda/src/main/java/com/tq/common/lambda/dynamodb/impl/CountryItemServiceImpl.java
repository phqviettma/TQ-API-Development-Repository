package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.CountryItemDao;
import com.tq.common.lambda.dynamodb.model.CountryItem;
import com.tq.common.lambda.dynamodb.service.CountryItemService;

public class CountryItemServiceImpl implements CountryItemService {

    private CountryItemDao m_countryItemDao;

    public CountryItemServiceImpl(CountryItemDao countryItemDao) {
        this.m_countryItemDao = countryItemDao;
    }

    @Override
    public void put(CountryItem countryItem) {
        m_countryItemDao.putItem(countryItem);
    }

    @Override
    public CountryItem load(String name) {
        return m_countryItemDao.loadItem(name);
    }
}
