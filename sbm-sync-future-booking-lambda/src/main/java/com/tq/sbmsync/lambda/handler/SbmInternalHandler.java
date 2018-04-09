package com.tq.sbmsync.lambda.handler;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.common.lambda.response.LambdaStatusResponse;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.sbmsync.lambda.model.SbmSyncReq;
import com.tq.simplybook.exception.SbmSDKException;

public interface SbmInternalHandler {
	LambdaStatusResponse handle(SbmSyncReq req) throws SbmSDKException, ClinikoSDKExeption, GoogleApiSDKException;
}
