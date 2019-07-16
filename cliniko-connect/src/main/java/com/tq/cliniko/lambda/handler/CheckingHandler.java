package com.tq.cliniko.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.model.Businesses;
import com.tq.cliniko.lambda.model.ClinikoPractitionerConnectReq;
import com.tq.cliniko.lambda.model.User;
import com.tq.cliniko.lambda.resp.ClinikoConnectStatusResponse;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.simplybook.exception.SbmSDKException;

public class CheckingHandler implements ConnectHandler {
	private static final Logger m_log = LoggerFactory.getLogger(CheckingHandler.class);
	private ClinikoSyncToSbmService clinikoSyncService = null;
	private ClinikoApiServiceBuilder apiServiceBuilder = null;

	public CheckingHandler(ClinikoSyncToSbmService clinikoSyncToSbmService, ClinikoApiServiceBuilder clinikoApiServiceBuilder) {
		this.clinikoSyncService = clinikoSyncToSbmService;
		this.apiServiceBuilder = clinikoApiServiceBuilder;
	}

	@Override
	public ClinikoConnectStatusResponse handle(ClinikoPractitionerConnectReq req)
			throws SbmSDKException, ClinikoSDKExeption {
		ClinikoSbmSync clinikoSbmSyncInfo = clinikoSyncService.queryEmail(req.getParams().getPractitionerEmail());
		if (clinikoSbmSyncInfo == null) {
			return buildResponse("disconnected", true);
		} else {
			String businessName = getBusinessName(clinikoSbmSyncInfo);
			if (businessName == null) {
				return buildResponse("disconnected", true);
			}
			return buildResponse("connected", true, businessName);
		}

	}
	
	private ClinikoConnectStatusResponse buildResponse(String status, boolean succeeded) {
		ClinikoConnectStatusResponse response = new ClinikoConnectStatusResponse();
		response.setStatus(status);
		response.setSucceeded(succeeded);
		return response;
	}

	private ClinikoConnectStatusResponse buildResponse(String status, boolean succeeded, String businessName) {
		ClinikoConnectStatusResponse response = buildResponse(status, succeeded);
		response.setBusinessName(businessName);
		return response;
	}
	
	private String getBusinessName(ClinikoSbmSync clinikoSbmSyncInfo) throws ClinikoSDKExeption {
		ClinikoAppointmentService clinikoService = apiServiceBuilder.build(clinikoSbmSyncInfo.getApiKey());
		User user = clinikoService.getAuthenticateUser();
		if (user == null) {
			m_log.info("The provided API Key is invalid. Therefore, Removing {}", clinikoSbmSyncInfo);
			clinikoSyncService.delete(clinikoSbmSyncInfo);
			m_log.info("Removed successfully. The practitioner need to reconnect");
			return null;
		}
		
		String businessId = clinikoSbmSyncInfo.getClinikoId().split("-")[0];
		Businesses business = clinikoService.getBusinessById(businessId);
		return business == null ? null : business.getBusiness_name();
	}
}
