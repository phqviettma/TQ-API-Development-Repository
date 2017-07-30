package com.tq.clickfunnel.lambda.configuration;

import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public interface ClickFunnelExternalService {

    public ContactServiceInf getContactServiceInf();

    public TokenServiceSbm getTokenServiceSbm();

    public ClientServiceSbm getClientServiceSbm();
}
