package com.tq.common.lambda.services;

import com.tq.inf.service.ContactServiceInf;
import com.tq.inf.service.DataServiceInf;
import com.tq.inf.service.OrderServiceInf;
import com.tq.inf.service.RecurringOrderInf;

public interface ISExternalService {

    public ContactServiceInf getContactServiceInf();
    
    public OrderServiceInf getOrderServiceInf();
    
    public DataServiceInf getDataServiceInf();
    
    public RecurringOrderInf getRecurringOrderInf();
}
