package com.tq.googlecalendar.service;

import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.resp.TokenResp;

public interface TokenGoogleCalendarService {
	TokenResp getToken(TokenReq req) throws GoogleApiSDKException;
}
