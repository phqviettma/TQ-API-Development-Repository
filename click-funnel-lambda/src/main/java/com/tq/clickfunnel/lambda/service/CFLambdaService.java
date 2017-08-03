package com.tq.clickfunnel.lambda.service;

import com.tq.inf.service.ContactServiceInf;
import com.tq.inf.service.OrderServiceInf;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public interface CFLambdaService {

    public ContactServiceInf getContactServiceInf();
    
    public OrderServiceInf getOrderServiceInf();

    public TokenServiceSbm getTokenServiceSbm();

    public ClientServiceSbm getClientServiceSbm();
}
