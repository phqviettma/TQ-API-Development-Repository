package com.tq.sbmsync.lambda.handler;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.response.LambdaStatusResponse;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.simplybook.exception.SbmSDKException;

public interface SbmInternalHandler {
	LambdaStatusResponse handle(SbmSyncFutureBookings sbmSyncFutureBooking) throws SbmSDKException, ClinikoSDKExeption, GoogleApiSDKException;
}
