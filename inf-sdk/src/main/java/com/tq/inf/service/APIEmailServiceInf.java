package com.tq.inf.service;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.OptQuery;

public interface APIEmailServiceInf {
    
    Boolean optIn(String apiName, String apiKey, OptQuery optInQuery) throws InfSDKExecption;

    Boolean optOut(String apiName, String apiKey, OptQuery optOutQuery) throws InfSDKExecption;

    Boolean getOptStatus(String apiName, String apiKey, String email) throws InfSDKExecption;
}
