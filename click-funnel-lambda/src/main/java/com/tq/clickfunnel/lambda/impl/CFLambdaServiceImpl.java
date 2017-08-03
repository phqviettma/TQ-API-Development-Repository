package com.tq.clickfunnel.lambda.impl;

import com.tq.clickfunnel.lambda.service.CFLambdaService;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.impl.ClientServiceImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CFLambdaServiceImpl implements CFLambdaService {

    private static ClickFunnelContextBuilder m_builder = ClickFunnelContextBuilder.DEFAILT;

    private ContactServiceInf m_contactServiceInf;

    private TokenServiceSbm m_tokenServiceSbm;

    private ClientServiceSbm m_clientServiceSbm;

    public static CFLambdaServiceImpl defaults() {
        return m_builder.registerINFContact(new ContactServiceImpl())
                      .registerSBMClientService(new ClientServiceImpl())
                      .registerSBMTokenService(new TokenServiceImpl())
                      .build();
    }

    public ClickFunnelContextBuilder builder() {
        return m_builder;
    }

    public ContactServiceInf getContactServiceInf() {
        return m_contactServiceInf;
    }

    public void setContactServiceInf(ContactServiceInf contactServiceInf) {
        this.m_contactServiceInf = contactServiceInf;
    }

    public TokenServiceSbm getTokenServiceSbm() {
        return m_tokenServiceSbm;
    }

    public void setTokenServiceSbm(TokenServiceSbm tokenServiceSbm) {
        this.m_tokenServiceSbm = tokenServiceSbm;
    }

    public ClientServiceSbm getClientServiceSbm() {
        return m_clientServiceSbm;
    }

    public void setClientServiceSbm(ClientServiceSbm clientServiceSbm) {
        this.m_clientServiceSbm = clientServiceSbm;
    }
}
