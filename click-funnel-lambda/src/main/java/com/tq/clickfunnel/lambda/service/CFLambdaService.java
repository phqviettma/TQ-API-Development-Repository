package com.tq.clickfunnel.lambda.service;

import com.tq.inf.service.ContactServiceInf;
import com.tq.inf.service.DataServiceInf;
import com.tq.inf.service.OrderServiceInf;
import com.tq.inf.service.RecurringOrderInf;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public interface CFLambdaService {

    public ContactServiceInf getContactServiceInf();
    
    public OrderServiceInf getOrderServiceInf();
    
    public DataServiceInf getDataServiceInf();
    
    public RecurringOrderInf getRecurringOrderInf();

    public TokenServiceSbm getTokenServiceSbm();

    public ClientServiceSbm getClientServiceSbm();
}
