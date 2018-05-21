package com.tq.googlecalendar.lambda.handler;

import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.lambda.resp.GoogleConnectStatusResponse;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;

public interface Handler {
	GoogleConnectStatusResponse  handle(GoogleRegisterReq req) throws GoogleApiSDKException, TrueQuitRegisterException, SbmSDKException, InfSDKExecption;
}
