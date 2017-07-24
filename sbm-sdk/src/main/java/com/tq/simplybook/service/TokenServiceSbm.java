package com.tq.simplybook.service;

import com.tq.simplybook.exception.SbmSDKException;

public interface TokenServiceSbm {
    String getToken(String companyLogin, String apiKey, String endpointLogin) throws SbmSDKException;
    
    String getUserToken(String companyLogin, String username, String password ,String endpointLogin) throws SbmSDKException;
}
