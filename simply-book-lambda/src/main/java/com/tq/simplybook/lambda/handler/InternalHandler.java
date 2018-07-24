package com.tq.simplybook.lambda.handler;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.lambda.model.PayloadCallback;

public interface InternalHandler {
    boolean handle(PayloadCallback payload) throws SbmSDKException, ClinikoSDKExeption, GoogleApiSDKException, InfSDKExecption;
}
