package com.tq.clickfunnel.lambda.configuration;

import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.impl.ClientServiceImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class BasicClickFunnelExertenalService implements ClickFunnelExternalService {

    private static ClickFunnelContextBuilder builder = ClickFunnelContextBuilder.DEFAILT;

    private ContactServiceInf contactServiceInf;

    private TokenServiceSbm tokenServiceSbm;

    private ClientServiceSbm clientServiceSbm;

    public static BasicClickFunnelExertenalService defaults() {
        return builder.registerINFContact(new ContactServiceImpl())
                      .registerSBMClientService(new ClientServiceImpl())
                      .registerSBMTokenService(new TokenServiceImpl())
                      .build();
    }

    public ClickFunnelContextBuilder builder() {
        return builder;
    }

    public ContactServiceInf getContactServiceInf() {
        return contactServiceInf;
    }

    public void setContactServiceInf(ContactServiceInf contactServiceInf) {
        this.contactServiceInf = contactServiceInf;
    }

    public TokenServiceSbm getTokenServiceSbm() {
        return tokenServiceSbm;
    }

    public void setTokenServiceSbm(TokenServiceSbm tokenServiceSbm) {
        this.tokenServiceSbm = tokenServiceSbm;
    }

    public ClientServiceSbm getClientServiceSbm() {
        return clientServiceSbm;
    }

    public void setClientServiceSbm(ClientServiceSbm clientServiceSbm) {
        this.clientServiceSbm = clientServiceSbm;
    }
}
