package com.tq.clickfunnel.lambda.configuration;

import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class ClickFunnelContextBuilder {

    private BasicClickFunnelExertenalService m_context = new BasicClickFunnelExertenalService();

    public static ClickFunnelContextBuilder DEFAILT = new ClickFunnelContextBuilder();

    public BasicClickFunnelExertenalService build() {
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

}
