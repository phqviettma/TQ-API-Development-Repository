package com.tq.inf.service;

import java.util.List;
import java.util.Map;

import com.tq.inf.exception.InfSDKExecption;

public interface RecurringOrderInf {
    public Object[] getAllRecurringOrder(String apiName, String apiKey, Integer contactId, final List<String> selectedFields) throws InfSDKExecption;
    
    public Object[] getRecurringOrderFromOriginatingOrderId(String apiName, String apiKey, Integer contactId, Integer originatingOrderId, final List<String> selectedFields) throws InfSDKExecption;
    
    public Map<?, ?> getLatestRecurringOrderFromProduct(String apiName, String apiKey, Integer contactId, Integer productId, final List<String> selectedFields) throws InfSDKExecption;
}
