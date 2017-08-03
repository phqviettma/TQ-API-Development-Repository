package com.tq.clickfunnel.lambda.service;

import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public interface CFLambdaService {

    public ContactServiceInf getContactServiceInf();

    public TokenServiceSbm getTokenServiceSbm();

    public ClientServiceSbm getClientServiceSbm();
}
