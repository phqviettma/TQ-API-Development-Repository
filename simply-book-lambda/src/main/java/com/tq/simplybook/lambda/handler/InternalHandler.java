package com.tq.simplybook.lambda.handler;

import com.tq.calendar.exception.GoogleApiSDKException;
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.lambda.model.PayloadCallback;

public interface InternalHandler {
    void handle(PayloadCallback payload) throws SbmSDKException, ClinikoSDKExeption, GoogleApiSDKException;
}
