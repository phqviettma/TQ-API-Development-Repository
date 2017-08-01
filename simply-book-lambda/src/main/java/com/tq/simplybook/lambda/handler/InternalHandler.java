package com.tq.simplybook.lambda.handler;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.lambda.context.Env;
import com.tq.simplybook.lambda.model.PayloadCallback;

public interface InternalHandler {
    void handle(Env env, PayloadCallback payload) throws SbmSDKException;
}
