package com.tq.cliniko.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.lambda.exception.ClinikoConnectException;
import com.tq.cliniko.lambda.model.ClinikoPractitionerConnectReq;
import com.tq.cliniko.lambda.resp.ClinikoConnectStatusResponse;
import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.service.ClinikoItemService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;

public class ClinikoDisconnectHandler implements ConnectHandler {
	private ClinikoSyncToSbmService clinikoSyncService = null;
	private ClinikoItemService clinikoItemService = null;
	private static final Logger m_log = LoggerFactory.getLogger(ClinikoDisconnectHandler.class);

	public ClinikoDisconnectHandler(ClinikoSyncToSbmService clinikoSyncService, ClinikoItemService clinikoItemService) {
		this.clinikoSyncService = clinikoSyncService;
		this.clinikoItemService = clinikoItemService;
	}

	@Override
	public ClinikoConnectStatusResponse handle(ClinikoPractitionerConnectReq req) throws ClinikoConnectException {
		ClinikoConnectStatusResponse response = new ClinikoConnectStatusResponse();
		String apiKey = req.getParams().getApiKey();
		ClinikoSbmSync clinikoSbmSync = clinikoSyncService.queryWithIndex(apiKey);
		if (clinikoSbmSync != null) {
			clinikoSyncService.delete(clinikoSbmSync);
			ClinikoSyncStatus clinikoItem = clinikoItemService.load(apiKey);
			if (clinikoItem != null) {
				clinikoItemService.delete(clinikoItem);
				m_log.info("Disconnect successfully");
			}
		} else {
			throw new ClinikoConnectException("This practitioner has not connected yet");
		}
		response.setSucceeded(true);
		return response;
	}

}
