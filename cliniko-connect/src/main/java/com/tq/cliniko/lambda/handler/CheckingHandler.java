package com.tq.cliniko.lambda.handler;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.model.ClinikoPractitionerConnectReq;
import com.tq.cliniko.lambda.resp.ClinikoConnectStatusResponse;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.simplybook.exception.SbmSDKException;

public class CheckingHandler implements ConnectHandler {
	private ClinikoSyncToSbmService clinikoSyncService = null;

	public CheckingHandler(ClinikoSyncToSbmService clinikoSyncToSbmService) {
		this.clinikoSyncService = clinikoSyncToSbmService;
	}

	@Override
	public ClinikoConnectStatusResponse handle(ClinikoPractitionerConnectReq req)
			throws SbmSDKException, ClinikoSDKExeption {
		ClinikoSbmSync clinikoSbmSyncInfo = clinikoSyncService.queryEmail(req.getParams().getPractitionerEmail());
		ClinikoConnectStatusResponse response = new ClinikoConnectStatusResponse();
		if (clinikoSbmSyncInfo == null) {
			response.setStatus("disconnected");
			response.setSucceeded(true);
			return response;
		} else {
			response.setStatus("connected");
			response.setSucceeded(true);
			return response;
		}

	}

}
