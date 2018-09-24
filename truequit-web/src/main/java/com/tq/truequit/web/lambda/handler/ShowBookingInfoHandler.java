package com.tq.truequit.web.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.tq.common.lambda.dynamodb.model.SbmBookingInfo;
import com.tq.common.lambda.dynamodb.service.SbmBookingInfoService;
import com.tq.truequit.web.lambda.response.ShowBookingInfoResponse;

public class ShowBookingInfoHandler implements BookingHandler {
	private static final Logger m_log = LoggerFactory.getLogger(ShowBookingInfoHandler.class);
	private SbmBookingInfoService sbmBookingInfoService =null;
	public ShowBookingInfoHandler( SbmBookingInfoService sbmBookingInfoService ) {
		this.sbmBookingInfoService = sbmBookingInfoService;
	}
	@Override
	public ShowBookingInfoResponse handle(ShowBookingRequest request) {
		QueryResultPage<SbmBookingInfo> bookings = sbmBookingInfoService.getListBooking(request.getParams().getEmail(),
				request.getParams().getSize());
		ShowBookingInfoResponse response = new ShowBookingInfoResponse(true, bookings.getResults(),
				bookings.getCount());
		m_log.info(String.format("response on showing booking action %s", response.getBookingLists().toString()));
		return response;
	}

}
