package com.tq.common.lambda.services;

import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public interface SBMExternalService {
    
    public TokenServiceSbm getTokenServiceSbm();

    public ClientServiceSbm getClientServiceSbm();
}
