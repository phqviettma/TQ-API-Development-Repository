package com.tq.calendar.service;

import com.tq.calendar.exception.GoogleApiSDKException;
import com.tq.calendar.req.TokenReq;
import com.tq.calendar.resp.TokenResp;

public interface TokenGoogleCalendarService {
	TokenResp getToken(TokenReq req) throws GoogleApiSDKException;
}
