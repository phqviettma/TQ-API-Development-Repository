package com.tq.inf.service;

import java.util.List;

import com.tq.inf.exception.InfSDKExecption;

public interface RecurringOrderInf {
    public Object[] getAllRecurringOrder(String apiName, String apiKey, Integer contactId, final List<String> selectedFields) throws InfSDKExecption;
}
