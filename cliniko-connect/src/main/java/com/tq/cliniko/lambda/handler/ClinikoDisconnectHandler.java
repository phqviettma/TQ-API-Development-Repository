package com.tq.cliniko.lambda.handler;

import com.tq.cliniko.lambda.exception.ClinikoConnectException;
import com.tq.cliniko.lambda.model.ClinikoPractitionerConnectReq;
import com.tq.cliniko.lambda.resp.ClinikoConnectStatusResponse;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoItemService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;

public class ClinikoDisconnectHandler implements ConnectHandler {
	private ClinikoSyncToSbmService clinikoSyncService = null;
	private ClinikoItemService clinikoItemService = null;
	private SbmSyncFutureBookingsService sbmSyncFutureBookingService = null;
	private SbmListBookingService sbmListBookingService = null;
	private ClinikoCompanyInfoService clinikoCompanyService;

	public ClinikoDisconnectHandler(ClinikoSyncToSbmService clinikoSyncService, ClinikoItemService clinikoItemService,
			SbmSyncFutureBookingsService sbmSyncFutureBookingService, SbmListBookingService sbmListBookingService, ClinikoCompanyInfoService clinikoCompanyService) {
		this.clinikoSyncService = clinikoSyncService;
		this.clinikoItemService = clinikoItemService;
		this.sbmSyncFutureBookingService = sbmSyncFutureBookingService;
		this.sbmListBookingService = sbmListBookingService;
		this.clinikoCompanyService = clinikoCompanyService;
	}

	@Override
	public ClinikoConnectStatusResponse handle(ClinikoPractitionerConnectReq req) throws ClinikoConnectException {
		ClinikoConnectStatusResponse response = new ClinikoConnectStatusResponse();
		String practitionerEmail = req.getParams().getPractitionerEmail();
		ClinikoSbmSync clinikoSbmSync = clinikoSyncService.queryEmail(practitionerEmail);
		if (clinikoSbmSync != null) {
			clinikoSyncService.delete(clinikoSbmSync);
			ClinikoSyncStatus clinikoItem = clinikoItemService.load(clinikoSbmSync.getApiKey());
			if (clinikoItem != null) {
				clinikoItemService.delete(clinikoItem);
				SbmSyncFutureBookings sbmSyncFutureBookings = sbmSyncFutureBookingService
						.load(clinikoSbmSync.getSbmId());
				SbmBookingList sbmListBooking = sbmListBookingService.load(clinikoSbmSync.getSbmId());
				ClinikoCompanyInfo clinikoCompanyInfo = clinikoCompanyService.load(clinikoSbmSync.getApiKey());
				if (sbmSyncFutureBookings != null && sbmListBooking != null && clinikoCompanyInfo != null) {
					sbmSyncFutureBookingService.delete(sbmSyncFutureBookings);
					sbmListBookingService.delete(sbmListBooking);
					clinikoCompanyService.delete(clinikoCompanyInfo);
				}
			}
		} else {
			throw new ClinikoConnectException("This practitioner has not connected yet");
		}
		response.setSucceeded(true);
		return response;
	}

}
