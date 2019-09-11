package com.tq.googlecalendar.lambda.handler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.lambda.resp.GoogleConnectStatusResponse;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.req.GetBookingReq;
import com.tq.simplybook.resp.GetBookingResp;
import com.tq.simplybook.service.TokenServiceSbm;

public class GoogleResyncCalendarHandler implements Handler {

	private static final Logger m_log = LoggerFactory.getLogger(GoogleResyncCalendarHandler.class);
	private GoogleCalendarDbService m_googleCalendarService;
	private GoogleCalendarModifiedSyncService m_calendarModifiedChannelService;
	private BookingServiceSbmImpl m_bookingService;
	private SbmListBookingService m_sbmBookingDBService;
	private Env m_eVariables;
	private TokenServiceSbm m_tokenServiceSbm;
	private SbmSyncFutureBookingsService m_sbmSyncFutureBookingService;
	private static final String BOOKING_TYPE = "non_cancelled";
	private static final String ORDER_BY = "start_date";
	
	public GoogleResyncCalendarHandler(GoogleCalendarDbService googleCalendarService,
			GoogleCalendarModifiedSyncService calendarModifiedChannelService, BookingServiceSbmImpl bookingService,
			SbmListBookingService sbmBookingDBService, Env eVariables, TokenServiceSbm tokenServiceSbm,
			SbmSyncFutureBookingsService sbmSyncFutureBookingService) {
		m_googleCalendarService = googleCalendarService;
		m_calendarModifiedChannelService = calendarModifiedChannelService;
		m_bookingService = bookingService;
		m_sbmBookingDBService = sbmBookingDBService;
		m_eVariables = eVariables;
		m_tokenServiceSbm = tokenServiceSbm;
		m_sbmSyncFutureBookingService = sbmSyncFutureBookingService;
	}

	@Override
	public GoogleConnectStatusResponse handle(GoogleRegisterReq req)
			throws GoogleApiSDKException, TrueQuitRegisterException, SbmSDKException, InfSDKExecption {
		String email = req.getParams().getEmail();
		m_log.info("Setting the re-sync process in the next sync for email: {}", email);
		List<GoogleCalendarSbmSync> googleCalendarSbmSyncList = m_googleCalendarService.queryEmail(email);
		if (googleCalendarSbmSyncList == null || googleCalendarSbmSyncList.isEmpty()) {
			throw new TrueQuitRegisterException("The email "+email+" does not exist"); 
		}
		
		GoogleCalendarSbmSync googleCalendarSbmSync = googleCalendarSbmSyncList.get(0);
		googleCalendarSbmSync.setLastQueryTimeMin(null);
		googleCalendarSbmSync.setNextSyncToken(null);
		googleCalendarSbmSync.setNextPageToken(null);
		googleCalendarSbmSync.setResync(true);
		m_googleCalendarService.put(googleCalendarSbmSync);
		
		String sbmId = googleCalendarSbmSync.getSbmId();
		saveFutureBookingToDB(sbmId);
		
		SbmSyncFutureBookings sbmFutureBooking = m_sbmSyncFutureBookingService.load(sbmId);
		sbmFutureBooking.setSyncStatus(1);
		m_sbmSyncFutureBookingService.put(sbmFutureBooking);
		
		GCModifiedChannel gcModifiedChannel = m_calendarModifiedChannelService.queryEmail(email).get(0);
		gcModifiedChannel.setCheckingStatus(1);
		m_calendarModifiedChannelService.put(gcModifiedChannel);
		
		m_log.info("Setting up the resync process is completed");
		GoogleConnectStatusResponse response = new GoogleConnectStatusResponse();
		response.setSucceeded(true);
		return response;
	}

	private void saveFutureBookingToDB(String sbmId) throws SbmSDKException {
		String eventId = sbmId.split("-")[0];
		String unitId = sbmId.split("-")[1];
		String companyLogin = m_eVariables.getSimplyBookCompanyLogin();
		String user = m_eVariables.getSimplyBookUser();
		String password = m_eVariables.getSimplyBookPassword();
		String loginEndPoint = m_eVariables.getSimplyBookServiceUrlLogin();
		String endpoint = m_eVariables.getSimplyBookAdminServiceUrl();
		String token = m_tokenServiceSbm.getUserToken(companyLogin, user, password, loginEndPoint);
		
		String dateFrom = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		GetBookingReq getBookingReq = new GetBookingReq(dateFrom, BOOKING_TYPE, ORDER_BY, Integer.valueOf(unitId),
				Integer.valueOf(eventId));
		List<GetBookingResp> bookingList = m_bookingService.getBookings(companyLogin, endpoint, token, getBookingReq);
		SbmBookingList sbmBookingItem = new SbmBookingList(sbmId, bookingList);
		m_sbmBookingDBService.put(sbmBookingItem);
		m_log.info("Added to database successfully " + sbmBookingItem.toString());
	}
}
