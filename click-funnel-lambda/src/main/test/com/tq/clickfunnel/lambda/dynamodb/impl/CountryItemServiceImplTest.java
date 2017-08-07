package com.tq.clickfunnel.lambda.dynamodb.impl;

import org.junit.Test;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.clickfunnel.lambda.dynamodb.model.CountryItem;
import com.tq.clickfunnel.lambda.dynamodb.service.CountryItemService;
import com.tq.clickfunnel.lambda.utils.DynanodbUtils;

import junit.framework.Assert;

public class CountryItemServiceImplTest {
    
    AmazonDynamoDB m_amazonDynamoDB = DynanodbUtils.getLocallyDynamoDB(); // Verify local or mock
    private CountryItemService m_countryItemService = new CountryItemServiceImpl(m_amazonDynamoDB);

    @Test
    public void testLoadCountryWithError() {
        //verify with key/value empty
        CountryItem emptyValue = m_countryItemService.load("");
        Assert.assertTrue(emptyValue == null);
    }
}
