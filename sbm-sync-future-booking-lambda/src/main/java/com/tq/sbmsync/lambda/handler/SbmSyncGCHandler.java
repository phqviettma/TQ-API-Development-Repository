package com.tq.sbmsync.lambda.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
import com.tq.common.lambda.response.LambdaStatusResponse;
import com.tq.common.lambda.utils.TimeUtils;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.req.Attendees;
import com.tq.googlecalendar.req.EventReq;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.resp.End;
import com.tq.googlecalendar.resp.EventResp;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.resp.Start;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.sbmsync.lambda.model.FindNewBooking;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.GetBookingResp;

public class SbmSyncGCHandler implements SbmInternalHandler {
	private GoogleCalendarDbService googleCalendarService = null;
	private Env eVariables = null;
	private static final String AGENT = "sbm";
	private TokenGoogleCalendarService tokenCalendarService = null;
	private SbmListBookingService sbmListBookingService = null;
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = null;
	private GoogleCalendarApiServiceBuilder apiServiceBuilder = null;
	private static final Logger m_log = LoggerFactory.getLogger(SbmSyncGCHandler.class);
	private SbmSyncFutureBookingsService sbmSyncFutureBookingService = null;

	public SbmSyncGCHandler(GoogleCalendarDbService googleCalendarService, Env eVariables,
			TokenGoogleCalendarService tokenCalendarService, SbmSyncFutureBookingsService sbmSyncFutureBookingService,
			SbmListBookingService sbmListBookingService, SbmGoogleCalendarDbService sbmGoogleCalendarService,
			GoogleCalendarApiServiceBuilder apiServiceBuilder) {
		this.googleCalendarService = googleCalendarService;
		this.eVariables = eVariables;
		this.tokenCalendarService = tokenCalendarService;
		this.sbmSyncFutureBookingService = sbmSyncFutureBookingService;
		this.sbmListBookingService = sbmListBookingService;
		this.sbmGoogleCalendarService = sbmGoogleCalendarService;
		this.apiServiceBuilder = apiServiceBuilder;
	}

	@Override
	public LambdaStatusResponse handle(SbmSyncFutureBookings sbmSyncFutureBooking)
			throws SbmSDKException, GoogleApiSDKException {
		LambdaStatusResponse response = new LambdaStatusResponse();
		List<GoogleCalendarSbmSync> googleChannelInfo = googleCalendarService
				.queryEmail(sbmSyncFutureBooking.getEmail());
		int syncNumber = 30;
		int processNumber = 0;
		if (!googleChannelInfo.isEmpty()) {
			TokenReq tokenReq = new TokenReq(eVariables.getGoogleClientId(), eVariables.getGoogleClientSecrets(),
					googleChannelInfo.get(0).getRefreshToken());
			TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);
			GoogleCalendarApiService googleApiService = apiServiceBuilder.build(tokenResp.getAccess_token());
			GoogleCalendarSettingsInfo settingInfo = googleApiService.getSettingInfo("timezone");
			for (GoogleCalendarSbmSync googleCalendarSbmSync : googleChannelInfo) {
				SbmBookingList bookingLists = sbmListBookingService.load(googleCalendarSbmSync.getSbmId());
				if (bookingLists != null) {
					if (!bookingLists.getBookingList().isEmpty()) {
						FindNewBooking newBookings = findNewBooking(bookingLists.getBookingList());
						m_log.info("Number new booking " + newBookings.getCount() + "New booking "
								+ newBookings.getBookingList().toString());
						Iterator<GetBookingResp> booking = newBookings.getBookingList().iterator();
						if (newBookings.getCount() > syncNumber) {
							processNumber = 0;
						} else {
							syncNumber = newBookings.getCount();
						}
						while (processNumber < syncNumber && booking.hasNext()) {
							GetBookingResp bookingResp = booking.next();

							SbmGoogleCalendar sbmGoogleCalendarSync = sbmGoogleCalendarService
									.load(Long.parseLong(bookingResp.getId()));
							if (sbmGoogleCalendarSync == null) {

								String sbmStartTime = TimeUtils.parseTime(bookingResp.getStart_date());
								String sbmEndTime = TimeUtils.parseTime(bookingResp.getEnd_date());
								Start start = new Start(sbmStartTime, settingInfo.getValue());
								End end = new End(sbmEndTime, settingInfo.getValue());
								List<Attendees> attendees = new ArrayList<>();
								attendees.add(new Attendees(bookingResp.getClient_email(), bookingResp.getClient()));
								EventReq eventReq = new EventReq(start, end, bookingResp.getEvent(), attendees,
										eVariables.getGoogleCalendarEventName());
								EventResp eventResp = googleApiService.createEvent(eventReq,
										googleCalendarSbmSync.getGoogleCalendarId());
								m_log.info("Create event successfully with value " + eventResp.toString());
								sbmGoogleCalendarSync = new SbmGoogleCalendar(Long.parseLong(bookingResp.getId()),
										eventResp.getId(), bookingResp.getClient_email(), 1, AGENT);

								sbmGoogleCalendarService.put(sbmGoogleCalendarSync);
								m_log.info("Add to database successfully " + sbmGoogleCalendarSync);
							}
							processNumber++;
						}
						if (newBookings.getCount() == 0) {
							sbmSyncFutureBooking.setSyncStatus(0);
							sbmSyncFutureBookingService.put(sbmSyncFutureBooking);
							m_log.info("There is no new booking");
						}

					} else {
						sbmSyncFutureBooking.setSyncStatus(0);
						sbmSyncFutureBookingService.put(sbmSyncFutureBooking);
						m_log.info("There is no new booking");
					}
				}

			}
		}
		response.setSucceeded(true);
		return response;
	}

	private FindNewBooking findNewBooking(List<GetBookingResp> bookingResp) {
		int num = 0;
		List<GetBookingResp> newBooking = new ArrayList<>();
		if (!bookingResp.isEmpty()) {
			for (GetBookingResp bookingItem : bookingResp) {
				SbmGoogleCalendar sbmGoogleCalendarSync = sbmGoogleCalendarService
						.load(Long.parseLong(bookingItem.getId()));
				if (sbmGoogleCalendarSync == null) {
					newBooking.add(bookingItem);
					num++;
				}
			}
		}
		return new FindNewBooking(num, newBooking);
	}

}
