package com.tq.googlecalendar.lambda.handler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalRenewService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.lambda.resp.GoogleConnectStatusResponse;
import com.tq.googlecalendar.req.Params;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.req.WatchEventReq;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.resp.WatchEventResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.req.GetBookingReq;
import com.tq.simplybook.resp.GetBookingResp;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.TokenServiceSbm;

public class GoogleConnectCalendarHandler implements Handler {
	private static final Logger m_log = LoggerFactory.getLogger(GoogleConnectCalendarHandler.class);
	private static final String PARAMS = "3600000";
	private SbmUnitService sbmUnitService = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private static final String BOOKING_TYPE = "non_cancelled";
	private static final String ORDER_BY = "start_date";
	private Env eVariables = null;
	private GoogleCalendarDbService googleCalendarService = null;
	private GoogleCalRenewService googleCalRenewService = null;
	private ContactItemService contactItemService = null;
	private GoogleCalendarApiServiceBuilder apiServiceBuilder = null;
	private TokenGoogleCalendarService tokenCalendarService = new TokenGoogleCalendarImpl();
	private GoogleCalendarModifiedSyncService calendarModifiedChannelService = null;
	private SbmSyncFutureBookingsService sbmSyncFutureBookingService = null;
	private BookingServiceSbmImpl bookingService = null;
	private SbmListBookingService sbmBookingDBService = null;
	public GoogleConnectCalendarHandler(Env eVariables, GoogleCalendarDbService googleCalendarService,
			ContactItemService contactItemService, TokenGoogleCalendarService tokenCalendarService,
			SbmUnitService sbmUnitService, TokenServiceSbm tokenServiceSbm,
			GoogleCalendarApiServiceBuilder apiServiceBuilder, GoogleCalRenewService googleCalRenewService,
			GoogleCalendarModifiedSyncService calendarModifiedChannelService,
			SbmSyncFutureBookingsService sbmSyncFutureBookingService, BookingServiceSbmImpl bookingService,
			SbmListBookingService sbmBookingDBService) {
		this.eVariables = eVariables;
		this.googleCalendarService = googleCalendarService;
		this.contactItemService = contactItemService;
		this.sbmUnitService = sbmUnitService;
		this.tokenCalendarService = tokenCalendarService;
		this.tokenServiceSbm = tokenServiceSbm;
		this.apiServiceBuilder = apiServiceBuilder;
		this.googleCalRenewService = googleCalRenewService;
		this.calendarModifiedChannelService = calendarModifiedChannelService;
		this.sbmSyncFutureBookingService = sbmSyncFutureBookingService;
		this.bookingService = bookingService;
		this.sbmBookingDBService = sbmBookingDBService;
	}

	@Override
	public GoogleConnectStatusResponse handle(GoogleRegisterReq req)
			throws GoogleApiSDKException, SbmSDKException, InfSDKExecption, TrueQuitRegisterException {
		GoogleConnectStatusResponse response = new GoogleConnectStatusResponse();
		String companyLogin = eVariables.getSimplyBookCompanyLogin();
		String user = eVariables.getSimplyBookUser();
		String password = eVariables.getSimplyBookPassword();
		String loginEndPoint = eVariables.getSimplyBookServiceUrlLogin();
		String endpoint = eVariables.getSimplyBookAdminServiceUrl();
		String googleEmail = req.getParams().getGoogleEmail();
		String sbmEmail = req.getParams().getEmail();
		List<GoogleCalendarSbmSync> googleCalendarSbmSync = googleCalendarService.queryEmail(sbmEmail);
		if (googleCalendarSbmSync.isEmpty()) {
			ContactItem contactItem = contactItemService.load(sbmEmail);
			if (contactItem == null) {
				throw new TrueQuitRegisterException("The email " + sbmEmail + " is not signed up yet ");
			}
			TokenReq tokenReq = new TokenReq(eVariables.getGoogleClientId(), eVariables.getGoogleClientSecrets(),
					req.getParams().getRefreshToken());

			TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);
			String token = tokenServiceSbm.getUserToken(companyLogin, user, password, loginEndPoint);
			List<UnitProviderInfo> unitInfos = sbmUnitService.getUnitList(companyLogin, endpoint, token, true, true, 1);
			boolean done = false;
			for (UnitProviderInfo unitInfo : unitInfos) {
				if (unitInfo.getEmail() != null && unitInfo.getEmail().equals(sbmEmail)) {
					if (unitInfo.getEvent_map() != null) {
						Set<String> eventInfos = unitInfo.getEvent_map().keySet();
						Iterator<String> it = eventInfos.iterator();
						if (it.hasNext()) {
							String eventId = it.next();
							String unitId = unitInfo.getId();
							String sbmId = eventId + "-" + unitId;
							Long lastQueryTime = buildLastCheckingTime();
							GoogleCalendarApiService googleApiService = apiServiceBuilder
									.build(tokenResp.getAccess_token());
							Params params = new Params(PARAMS);

							for (String googleCalendarId : req.getParams().getGoogleCalendarId()) {
								UUID uuid = UUID.randomUUID();
								String channelId = uuid.toString();
								WatchEventReq watchEventReq = new WatchEventReq(channelId, "web_hook",
										eVariables.getGoogleNotifyDomain(), params);
								WatchEventResp watchEventResp = googleApiService.watchEvent(watchEventReq,
										googleCalendarId);

								m_log.info("Watch calendar successfully with response: " + watchEventResp);
								String refreshToken = req.getParams().getRefreshToken();
								Long checkingTime = buildCheckingTime(watchEventResp.getExpiration());
								saveToMappingDB(req, googleEmail, sbmEmail, sbmId, googleCalendarId, watchEventResp,
										refreshToken);
								saveToCheckingExpirationDB(googleEmail, lastQueryTime, channelId, watchEventResp,
										refreshToken, checkingTime);
								saveToSyncedUpDB(sbmEmail, sbmId, googleCalendarId, channelId);
								saveFutureBookingToDB(companyLogin, endpoint, token, eventId, unitId, sbmId);
								
							}
							done = true;
							break;
						}
					}

				}
			}
			if (!done) {
				throw new TrueQuitRegisterException(
						"There is no Simplybook.me service provider associated to the provided e-mail " + sbmEmail);
			}
		} else {
			throw new TrueQuitRegisterException("The email " + sbmEmail + " is already connected");
		}
		response.setSucceeded(true);
		return response;

	}

	private void saveFutureBookingToDB(String companyLogin, String endpoint, String token, String eventId,
			String unitId, String sbmId) throws SbmSDKException {
		String dateFrom = new SimpleDateFormat("yyyy-MM-dd")
				.format(Calendar.getInstance().getTime());
		GetBookingReq getBookingReq = new GetBookingReq(dateFrom, BOOKING_TYPE, ORDER_BY,
				Integer.valueOf(unitId), Integer.valueOf(eventId));
		List<GetBookingResp> bookingList = bookingService.getBookings(companyLogin, endpoint,
				token, getBookingReq);
		SbmBookingList sbmBookingItem = new SbmBookingList(sbmId, bookingList);
		sbmBookingDBService.put(sbmBookingItem);
		m_log.info("Added to database successfully " + sbmBookingItem.toString());
	}

	private void saveToSyncedUpDB(String sbmEmail, String sbmId, String googleCalendarId, String channelId) {
		long timeStamp = Calendar.getInstance().getTimeInMillis();
		GCModifiedChannel modifiedChannelItem = new GCModifiedChannel(googleCalendarId, -1,
				timeStamp, sbmEmail, channelId);
		calendarModifiedChannelService.put(modifiedChannelItem);
		SbmSyncFutureBookings sbmSyncFutureBookingItem = new SbmSyncFutureBookings(sbmId, null,
				sbmEmail, 1, timeStamp);
		sbmSyncFutureBookingService.put(sbmSyncFutureBookingItem);
	}

	private void saveToCheckingExpirationDB(String googleEmail, Long lastQueryTime, String channelId,
			WatchEventResp watchEventResp, String refreshToken, Long checkingTime) {
		GoogleRenewChannelInfo channelInfo = new GoogleRenewChannelInfo(checkingTime,
				watchEventResp.getExpiration(), refreshToken,
				watchEventResp.getWatchResourceId(), lastQueryTime, channelId, googleEmail);
		googleCalRenewService.put(channelInfo);
		m_log.info("Added to database successfully " + channelInfo.toString());
	}

	private void saveToMappingDB(GoogleRegisterReq req, String googleEmail, String sbmEmail, String sbmId,
			String googleCalendarId, WatchEventResp watchEventResp, String refreshToken) {
		GoogleCalendarSbmSync calendarSbm = new GoogleCalendarSbmSync(sbmId, sbmEmail,
				googleCalendarId, req.getParams().getLastName(), req.getParams().getFirstName(),
				refreshToken, googleEmail, watchEventResp.getWatchResourceId(),
				watchEventResp.getId());
		googleCalendarService.put(calendarSbm);
	}

	private static Long buildCheckingTime(long expirationTime) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTimeInMillis(expirationTime);
		cal.add(Calendar.DATE, -1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		Date date = cal.getTime();
		long checkingDate = date.getTime();
		return checkingDate;

	}

	private static Long buildLastCheckingTime() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		Date date = cal.getTime();
		long lastQueryTime = date.getTime();
		return lastQueryTime;

	}
}
