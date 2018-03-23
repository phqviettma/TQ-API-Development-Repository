package com.tq.cliniko.lambda.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.lambda.exception.ClinikoConnectException;
import com.tq.cliniko.lambda.model.ClinikoPractitionerConnectReq;
import com.tq.cliniko.lambda.resp.ClinikoConnectStatusResponse;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoAppointmentWrapper;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppointment;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.LatestClinikoAppointmentService;

public class ClinikoDisconnectHandler implements ConnectHandler {
	private ClinikoSyncToSbmService clinikoSyncService = null;
	private LatestClinikoAppointmentWrapper latestClinikoServiceWrapper = null;
	private LatestClinikoAppointmentService latestClinikoService = null;
	private static final Logger m_log = LoggerFactory.getLogger(ClinikoDisconnectHandler.class);

	public ClinikoDisconnectHandler(ClinikoSyncToSbmService clinikoSyncService,
			LatestClinikoAppointmentWrapper latestClinikoServiceWrapper,
			LatestClinikoAppointmentService latestClinikoService) {
		this.clinikoSyncService = clinikoSyncService;
		this.latestClinikoServiceWrapper = latestClinikoServiceWrapper;
		this.latestClinikoService = latestClinikoService;
	}

	@Override
	public ClinikoConnectStatusResponse handle(ClinikoPractitionerConnectReq req) throws ClinikoConnectException {
		ClinikoConnectStatusResponse response = new ClinikoConnectStatusResponse();
		String apiKey = req.getParams().getApiKey();
		ClinikoSbmSync clinikoSbmSync = clinikoSyncService.load(apiKey);
		if (clinikoSbmSync != null) {
			clinikoSyncService.delete(clinikoSbmSync);
			List<LatestClinikoAppointment> clinikoAppt = latestClinikoService.queryItem();
			for (LatestClinikoAppointment appointment : clinikoAppt) {
				if (apiKey.equals(appointment.getApiKey())) {
					latestClinikoServiceWrapper.delete(appointment);
				}
			}
			m_log.info("Disconnect successfully");
		} else {
			throw new ClinikoConnectException("This practitioner has not connected yet");
		}
		response.setSucceeded(true);
		return response;
	}

}
