package com.tq.truequit.web.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.tq.common.lambda.dynamodb.model.SbmBookingInfo;
import com.tq.common.lambda.dynamodb.service.SbmBookingInfoService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.truequit.web.lambda.response.ShowBookingInfoResponse;

public class TQShowMemberBooking implements BookingHandler {
	private SbmBookingInfoService sbmBookingInfoService = null;
	private static final Logger m_log = LoggerFactory.getLogger(TQShowMemberBooking.class);
	public TQShowMemberBooking(SbmBookingInfoService sbmBookingService) {
		this.sbmBookingInfoService = sbmBookingService;
	}

	@Override
	public ShowBookingInfoResponse handle(ShowBookingRequest request) throws SbmSDKException, InfSDKExecption {
		m_log.info("Received request " + request);
		QueryResultPage<SbmBookingInfo> bookings = sbmBookingInfoService.queryClientEmail(request.getParams().getSize(),request.getParams().getClientEmail());
		m_log.info("Get list bookings" + bookings.getResults());
		ShowBookingInfoResponse response = new ShowBookingInfoResponse(true, bookings.getResults(), bookings.getCount());
		return response;
	}

}
