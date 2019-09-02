package com.tq.cliniko.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.exception.ClinikoConnectException;
import com.tq.cliniko.lambda.model.ClinikoPractitionerConnectReq;
import com.tq.cliniko.lambda.resp.ClinikoConnectStatusResponse;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.service.ClinikoItemService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.simplybook.exception.SbmSDKException;

public class ClinikoResyncHandler implements ConnectHandler {

	private static final Logger m_log = LoggerFactory.getLogger(ClinikoResyncHandler.class);
	private ClinikoItemService m_clinikoItemService = null;
	private ClinikoSyncToSbmService m_clinikoSyncService = null;
	
	public ClinikoResyncHandler(ClinikoSyncToSbmService clinikoSyncService, ClinikoItemService clinikoItemService) {
		m_clinikoSyncService = clinikoSyncService;
		m_clinikoItemService = clinikoItemService;
	}
	@Override
	public ClinikoConnectStatusResponse handle(ClinikoPractitionerConnectReq req)
			throws SbmSDKException, ClinikoSDKExeption {
		String email = req.getParams().getPractitionerEmail();
		ClinikoSbmSync clinikoSbmSync = m_clinikoSyncService.queryEmail(email);
		if (clinikoSbmSync == null) {
			throw new ClinikoConnectException("The practitioner email "+email+" does not exist") ;
		}

		String apiKey = clinikoSbmSync.getApiKey();
		m_log.info("Setting the re-sync process in the next sync for apiKey: {}", apiKey);
		ClinikoSyncStatus clinikoSyncStatus = m_clinikoItemService.queryWithIndex(apiKey);
		
		if (clinikoSyncStatus != null) {
			clinikoSyncStatus.setLatestTime(null);
			clinikoSyncStatus.setReSync(true);
			m_clinikoItemService.put(clinikoSyncStatus);
		} else {
			throw new ClinikoConnectException("The provided API Key does not exist");
		}
		m_log.info("Setting up the resync process is completed");
		ClinikoConnectStatusResponse response = new ClinikoConnectStatusResponse();
		response.setSucceeded(true);
		return response;
	}

}
