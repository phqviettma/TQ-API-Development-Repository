package com.tq.sbmsync.lambda.handler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.common.lambda.response.LambdaStatusResponse;
import com.tq.common.lambda.utils.TimeUtils;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceImpl;
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
import com.tq.sbmsync.lambda.model.SbmSyncReq;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.req.GetBookingReq;
import com.tq.simplybook.resp.GetBookingResp;
import com.tq.simplybook.service.TokenServiceSbm;

public class SbmSyncGCHandler implements SbmInternalHandler {
	private GoogleCalendarDbService googleCalendarService = null;
	private Env eVariables = null;
	private BookingServiceSbmImpl bookingSbmService = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private static final String BOOKING_TYPE = "non_cancelled";
	private static final String ORDER_BY = "start_date";
	private static final String AGENT = "sbm";
	private TokenGoogleCalendarService tokenCalendarService = null;
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = null;
	private static final Logger m_log = LoggerFactory.getLogger(SbmSyncGCHandler.class);

	@Override
	public LambdaStatusResponse handle(SbmSyncReq req) throws SbmSDKException, GoogleApiSDKException {
		LambdaStatusResponse response = new LambdaStatusResponse();
		GoogleCalendarSbmSync googleChannelInfo = googleCalendarService.query(req.getParams().getGoogleCalendarEmail());
		if (googleChannelInfo != null) {
			String sbmId[] = googleChannelInfo.getSbmId().split("-");
			Integer unitId = Integer.valueOf(sbmId[1]);
			Integer serviceId = Integer.valueOf(sbmId[0]);
			String companyLogin = eVariables.getSimplyBookCompanyLogin();
			String user = eVariables.getSimplyBookUser();
			String password = eVariables.getSimplyBookPassword();
			String loginEndPoint = eVariables.getSimplyBookServiceUrlLogin();
			String endpoint = eVariables.getSimplyBookAdminServiceUrl();
			String dateFrom = Calendar.getInstance().toString();
			String token = tokenServiceSbm.getUserToken(companyLogin, user, password, loginEndPoint);
			GetBookingReq getBookingReq = new GetBookingReq(dateFrom, BOOKING_TYPE, ORDER_BY, unitId, serviceId);
			List<GetBookingResp> bookingList = bookingSbmService.getBookings(companyLogin, endpoint, token,
					getBookingReq);
			Iterator<GetBookingResp> booking = bookingList.iterator();
			while (booking.hasNext()) {
				GetBookingResp bookingResp = booking.next();
				TokenReq tokenReq = new TokenReq(eVariables.getGoogleClientId(), eVariables.getGoogleClientSecrets(),
						googleChannelInfo.getRefreshToken());
				TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);
				GoogleCalendarApiService googleApiService = new GoogleCalendarApiServiceImpl(
						tokenResp.getAccess_token());
				GoogleCalendarSettingsInfo settingInfo = googleApiService.getSettingInfo("timezone");
				String sbmStartTime = TimeUtils.parseTime(bookingResp.getStart_date());
				String sbmEndTime = TimeUtils.parseTime(bookingResp.getEnd_date());
				Start start = new Start(sbmStartTime, settingInfo.getValue());
				End end = new End(sbmEndTime, settingInfo.getValue());
				List<Attendees> attendees = new ArrayList<>();
				attendees.add(new Attendees(bookingResp.getClient_email(), bookingResp.getClient()));
				EventReq eventReq = new EventReq(start, end, bookingResp.getEvent(), attendees,
						eVariables.getGoogleCalendarEventName());
				EventResp eventResp = googleApiService.createEvent(eventReq);
				m_log.info("Create event successfully with value " + eventResp.toString());
				SbmGoogleCalendar sbmGoogleCalendarSync = new SbmGoogleCalendar(Long.parseLong(bookingResp.getId()),
						eventResp.getId(), bookingResp.getClient_email(), 1, AGENT);
				if (sbmGoogleCalendarSync != null) {
					sbmGoogleCalendarService.put(sbmGoogleCalendarSync);
					m_log.info("Add to database successfully " + sbmGoogleCalendarSync);
				}
			}
		}
		response.setSucceeded(true);
		return response;
	}

}
