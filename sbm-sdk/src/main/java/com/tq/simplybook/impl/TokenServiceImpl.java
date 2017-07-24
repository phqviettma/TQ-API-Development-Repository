package com.tq.simplybook.impl;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.TokenReq;
import com.tq.simplybook.req.UserTokenReq;
import com.tq.simplybook.resp.Token;
import com.tq.simplybook.service.TokenServiceSbm;
import com.tq.simplybook.utils.SbmUtils;

public class TokenServiceImpl implements TokenServiceSbm {

    @Override
    public String getToken(String companyLogin, String apiKey, String endpointLogin) throws SbmSDKException {
        TokenReq req = new TokenReq(companyLogin, apiKey);
        String jsonResp = SbmUtils.invokeTokenSignIn("getToken", endpointLogin, req);
        return SbmUtils.readValueForObject(jsonResp, Token.class).getTokenID();
    }

    @Override
    public String getUserToken(String companyLogin, String username, String password, String endpointLogin) throws SbmSDKException {
        UserTokenReq req = new UserTokenReq(companyLogin, username, password);
        String jsonResp = SbmUtils.invokeUserTokenSignIn(endpointLogin, "getUserToken", req);
        return SbmUtils.readValueForObject(jsonResp, Token.class).getTokenID();
    }

}
