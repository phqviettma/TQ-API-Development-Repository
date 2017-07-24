package com.tq.inf.service;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddSubscriptionQuery;
import com.tq.inf.query.BlankOrderQuery;

public interface InvoiceServiceInf {
    
    Integer createBlankOrder(String apiName, String apiKey, BlankOrderQuery baBlankOrder) throws InfSDKExecption;
    
    Integer deleteSubscription(String apiName, String apiKey, Integer subscriptionId) throws InfSDKExecption;
    
    Integer deleteInvoice(String apiName, String apiKey, Integer invoiceID) throws InfSDKExecption;
    
    Integer addRecurringOrder(String apiName, String apiKey, AddSubscriptionQuery subQuery) throws InfSDKExecption;
    
    Integer createInvoiceForRecurring(String apiName, String apiKey,final Integer recurringOrderId) throws InfSDKExecption;
}
