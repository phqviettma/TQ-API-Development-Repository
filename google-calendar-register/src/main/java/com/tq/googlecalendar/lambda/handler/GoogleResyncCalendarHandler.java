package com.tq.googlecalendar.lambda.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.lambda.resp.GoogleConnectStatusResponse;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;

public class GoogleResyncCalendarHandler implements Handler {

	private static final Logger m_log = LoggerFactory.getLogger(GoogleResyncCalendarHandler.class);
	private GoogleCalendarDbService m_googleCalendarService;
	private GoogleCalendarModifiedSyncService m_calendarModifiedChannelService;
	public GoogleResyncCalendarHandler(GoogleCalendarDbService googleCalendarService, GoogleCalendarModifiedSyncService calendarModifiedChannelService) {
		m_googleCalendarService = googleCalendarService;
		m_calendarModifiedChannelService = calendarModifiedChannelService;
	}

	@Override
	public GoogleConnectStatusResponse handle(GoogleRegisterReq req)
			throws GoogleApiSDKException, TrueQuitRegisterException, SbmSDKException, InfSDKExecption {
		String email = req.getParams().getEmail();
		m_log.info("Starting the resync process for email: {}", email);
		List<GoogleCalendarSbmSync> googleCalendarSbmSyncList = m_googleCalendarService.queryEmail(email);
		if (googleCalendarSbmSyncList == null || googleCalendarSbmSyncList.isEmpty()) {
			throw new TrueQuitRegisterException("The email "+email+" does not exist"); 
		}
		
		GoogleCalendarSbmSync googleCalendarSbmSync = googleCalendarSbmSyncList.get(0);
		googleCalendarSbmSync.setLastQueryTimeMin(null);
		googleCalendarSbmSync.setNextSyncToken(null);
		googleCalendarSbmSync.setNextPageToken(null);
		m_googleCalendarService.put(googleCalendarSbmSync);
		
		GCModifiedChannel gcModifiedChannel = m_calendarModifiedChannelService.queryEmail(email).get(0);
		gcModifiedChannel.setCheckingStatus(1);
		m_calendarModifiedChannelService.put(gcModifiedChannel);
		
		m_log.info("The resync process is completed");
		GoogleConnectStatusResponse response = new GoogleConnectStatusResponse();
		response.setSucceeded(true);
		return response;
	}

}
