package com.tq.truequit.web.lambda.handler;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.truequit.web.lambda.response.ShowBookingInfoResponse;

public interface BookingHandler {
	ShowBookingInfoResponse handle(ShowBookingRequest request) throws SbmSDKException, InfSDKExecption;
}
