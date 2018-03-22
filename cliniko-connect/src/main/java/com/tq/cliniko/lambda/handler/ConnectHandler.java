package com.tq.cliniko.lambda.handler;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.model.ClinikoPractitionerConnectReq;
import com.tq.cliniko.lambda.resp.ClinikoConnectStatusResponse;
import com.tq.simplybook.exception.SbmSDKException;

public interface ConnectHandler {
	ClinikoConnectStatusResponse handle(ClinikoPractitionerConnectReq req) throws SbmSDKException, ClinikoSDKExeption;
}
