package com.tq.cliniko.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.lambda.exception.ClinikoConnectException;
import com.tq.cliniko.lambda.model.ClinikoPractitionerConnectReq;
import com.tq.cliniko.lambda.resp.ClinikoConnectStatusResponse;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.ClinikoItemService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;

public class ClinikoDisconnectHandler implements ConnectHandler {
	private ClinikoSyncToSbmService clinikoSyncService = null;
	private ClinikoItemService clinikoItemService = null;
	private SbmSyncFutureBookingsService sbmSyncFutureBookingService = null;
	private SbmListBookingService sbmListBookingService = null;
	private static final Logger m_log = LoggerFactory.getLogger(ClinikoDisconnectHandler.class);

	public ClinikoDisconnectHandler(ClinikoSyncToSbmService clinikoSyncService, ClinikoItemService clinikoItemService,
			SbmSyncFutureBookingsService sbmSyncFutureBookingService, SbmListBookingService sbmListBookingService) {
		this.clinikoSyncService = clinikoSyncService;
		this.clinikoItemService = clinikoItemService;
		this.sbmSyncFutureBookingService = sbmSyncFutureBookingService;
		this.sbmListBookingService = sbmListBookingService;
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
				SbmSyncFutureBookings sbmSyncFutureBookings = sbmSyncFutureBookingService
						.load(clinikoSbmSync.getSbmId());
				sbmSyncFutureBookingService.delete(sbmSyncFutureBookings);
				SbmBookingList sbmListBooking = sbmListBookingService.load(clinikoSbmSync.getSbmId());
				if (sbmListBooking != null) {
					sbmListBookingService.delete(sbmListBooking);
				}
			}
		} else {
			throw new ClinikoConnectException("This practitioner has not connected yet");
		}
		response.setSucceeded(true);
		return response;
	}

}
