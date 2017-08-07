package com.tq.clickfunnel.lambda.impl;

import com.tq.inf.service.ContactServiceInf;
import com.tq.inf.service.DataServiceInf;
import com.tq.inf.service.OrderServiceInf;
import com.tq.inf.service.RecurringOrderInf;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class ClickFunnelContextBuilder {

    private CFLambdaServiceImpl m_context = new CFLambdaServiceImpl();

    public static ClickFunnelContextBuilder DEFAILT = new ClickFunnelContextBuilder();

    public CFLambdaServiceImpl build() {
        return m_context;
    }

    public ClickFunnelContextBuilder registerINFContact(ContactServiceInf contactServiceInf) {
        m_context.setContactServiceInf(contactServiceInf);
        return this;
    }

    public ClickFunnelContextBuilder registerSBMTokenService(TokenServiceSbm tokenServiceSbm) {
        m_context.setTokenServiceSbm(tokenServiceSbm);
        return this;
    }

    public ClickFunnelContextBuilder registerSBMClientService(ClientServiceSbm clientServiceSbm) {
        m_context.setClientServiceSbm(clientServiceSbm);
        return this;
    }
    
    public ClickFunnelContextBuilder registerINFOrder(OrderServiceInf orderServiceInf) {
        m_context.setOrderServiceInf(orderServiceInf);
        return this;
    }
    
    public ClickFunnelContextBuilder registerDataServiceInf(DataServiceInf dataServiceInf) {
        m_context.setDataServiceInf(dataServiceInf);
        return this;
    }
    
    public ClickFunnelContextBuilder registerRecurringOrderServiceInf(RecurringOrderInf recurringOrderInf) {
        m_context.setRecurringOrderServiceInf(recurringOrderInf);
        return this;
    }

}
