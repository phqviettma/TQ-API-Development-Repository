package com.tq.simplybook.lambda.handler;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.lambda.req.ClinikoBodyRequest;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.common.lambda.utils.TimeUtils;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.req.EventReq;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.resp.End;
import com.tq.googlecalendar.resp.EventResp;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.resp.Start;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddDataQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.lambda.util.CustomField;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class ChangeInternalHandler implements InternalHandler {
	private Env env = null;
	private static final Logger m_log = LoggerFactory.getLogger(ChangeInternalHandler.class);
	private BookingServiceSbm bookingService = null;
	private TokenServiceSbm tokenService = null;
	private CountryItemService countryItemService = null;
	private ContactServiceInf contactService = null;
	private ContactItemService contactItemService = null;
	private ClinikoSyncToSbmService clinikoSyncToSbmService = null;
	private GoogleCalendarDbService googleCalendarService = null;
	private TokenGoogleCalendarService tokenCalendarService = null;
	private ClinikoApiServiceBuilder clinikoApiServiceBuilder = null;
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = null;
	private GoogleCalendarApiServiceBuilder googleApiBuilder = null;
	private SbmClinikoSyncService sbmClinikoService = null;

	public ChangeInternalHandler(Env env, BookingServiceSbm bookingService, TokenServiceSbm tokenService,
			CountryItemService countryItemService, ContactServiceInf contactService,
			ContactItemService contactItemService, ClinikoSyncToSbmService clinikoSyncToSbmService,
			GoogleCalendarDbService googleCalendarService, TokenGoogleCalendarService tokenCalendarService,
			ClinikoApiServiceBuilder clinikoApiServiceBuilder, SbmGoogleCalendarDbService sbmGoogleCalendarService,
			GoogleCalendarApiServiceBuilder googleApiBuilder, SbmClinikoSyncService sbmClinikoService) {
		this.env = env;
		this.bookingService = bookingService;
		this.tokenCalendarService = tokenCalendarService;
		this.tokenService = tokenService;
		this.countryItemService = countryItemService;
		this.clinikoSyncToSbmService = clinikoSyncToSbmService;
		this.googleCalendarService = googleCalendarService;
		this.clinikoApiServiceBuilder = clinikoApiServiceBuilder;
		this.sbmGoogleCalendarService = sbmGoogleCalendarService;
		this.googleApiBuilder = googleApiBuilder;
		this.sbmClinikoService = sbmClinikoService;
		this.contactService = contactService;
		this.contactItemService = contactItemService;
	}

	@Override
	public boolean handle(PayloadCallback payload)
			throws SbmSDKException, ClinikoSDKExeption, GoogleApiSDKException, InfSDKExecption {
		String companyLogin = env.getSimplyBookCompanyLogin();
		String user = env.getSimplyBookUser();
		String password = env.getSimplyBookPassword();
		String loginEndPoint = env.getSimplyBookServiceUrlLogin();
		String adminEndPoint = env.getSimplyBookAdminServiceUrl();
		String token = tokenService.getUserToken(companyLogin, user, password, loginEndPoint);
		Long bookingId = payload.getBooking_id();
		BookingInfo bookingInfo = bookingService.getBookingInfo(companyLogin, adminEndPoint, token, bookingId);
		if (bookingInfo == null) {
			throw new SbmSDKException("There is no booking content asociated to the booking id: " + bookingId);
		}
		ContactItem clientISContact = contactItemService.load(bookingInfo.getClient_email());
		if (clientISContact.getEmail() == null && clientISContact.getClient().getContactId() == null) {
			throw new InfSDKExecption(String.format("There do not have email %s in IS", bookingInfo.getClient_email()));
		}

		boolean processed = false;
		String sbmId = bookingInfo.getEvent_id() + "-" + bookingInfo.getUnit_id();
		ClinikoSbmSync clinikoSbmSync = clinikoSyncToSbmService.load(sbmId);
		if (clinikoSbmSync != null) {
			processed = executeWithCliniko(bookingInfo, clinikoSbmSync);
		}
		List<GoogleCalendarSbmSync> sbmGoogle = googleCalendarService.queryEmail(bookingInfo.getUnit_email());
		if (!sbmGoogle.isEmpty()) {
			processed = excuteWithGoogleCalendar(bookingInfo, sbmGoogle.get(0).getRefreshToken(),
					sbmGoogle.get(0).getGoogleCalendarId());
		}
		if (processed) {
			m_log.info("The booking is synced to Cliniko/Google Calendar");
		} else {
			m_log.info("The booking is synced neither to Cliniko nor Google Calendar");
		}
		updateCustomFieldIS(bookingInfo, clientISContact.getClient().getContactId());
		return processed;
	}

	private boolean excuteWithGoogleCalendar(BookingInfo bookingInfo, String refreshToken,
			String googleCalendarId) throws GoogleApiSDKException {
		TokenReq tokenReq = new TokenReq(env.getGoogleClientId(), env.getGoogleClientSecrets(), refreshToken);
		TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);
		GoogleCalendarApiService googleApiService = googleApiBuilder.build(tokenResp.getAccess_token());
		SbmGoogleCalendar sbmGoogleDbItem = sbmGoogleCalendarService.load(Long.parseLong(bookingInfo.getId(),10));
		if (sbmGoogleDbItem != null) {
			GoogleCalendarSettingsInfo settingInfo = googleApiService.getSettingInfo("timezone");
			String sbmStartTime = TimeUtils.parseTime(bookingInfo.getStart_date_time());
			String sbmEndTime = TimeUtils.parseTime(bookingInfo.getEnd_date_time());
			Start start = new Start(sbmStartTime, settingInfo.getValue());
			End end = new End(sbmEndTime, settingInfo.getValue());
			EventReq events = new EventReq();
			events.setStart(start);
			events.setEnd(end);
			EventResp eventResp = googleApiService.updateEvent(events, googleCalendarId, sbmGoogleDbItem.getEventId());
			m_log.info(String.format("update successfully event %s", eventResp.toString()));
			return true;
		} else {
			m_log.info(String.format("Can not find booking id %d ", bookingInfo.getId()));
			return false;
		}
	}

	private boolean executeWithCliniko(BookingInfo bookingInfo, ClinikoSbmSync clinikoSbmSync)
			throws ClinikoSDKExeption {
		ClinikoAppointmentService clinikoApptService = clinikoApiServiceBuilder.build(clinikoSbmSync.getApiKey());
		SbmCliniko sbmCliniko = sbmClinikoService.load(Long.parseLong(bookingInfo.getId()));
		if (sbmCliniko != null) {
			Settings settings = clinikoApptService.getAllSettings();
			String country = settings.getAccount().getCountry();
			String time_zone = settings.getAccount().getTime_zone();
			DateTimeZone timeZone = DateTimeZone.forID(country + "/" + time_zone);
			String sbmStartTime = TimeUtils.parseTime(bookingInfo.getStart_date_time());
			String sbmEndTime = TimeUtils.parseTime(bookingInfo.getEnd_date_time());
			DateTime start_time = new DateTime(sbmStartTime, timeZone);
			DateTime clinikoStartTime = start_time.withZone(DateTimeZone.UTC);
			DateTime endTime = new DateTime(sbmEndTime, timeZone);
			DateTime clinikoEndTime = endTime.withZone(DateTimeZone.UTC);
			ClinikoBodyRequest bodyRequest = new ClinikoBodyRequest(clinikoStartTime.toString(),
					clinikoEndTime.toString());
			AppointmentInfo updatedAppointment = clinikoApptService.updateAppointment(bodyRequest,
					sbmCliniko.getClinikoId());
			m_log.info(String.format("Updated appointment successfully %s ", updatedAppointment.toString()));
			return true;
		} else {
			m_log.info(String.format("Can not find booking id %d in database", bookingInfo.getId()));
			return false;
		}
	}

	private void updateCustomFieldIS(BookingInfo bookingInfo, Integer ifContactId) {
		Map<String, String> updateRecord = CustomField.buildInfCustomField(countryItemService, env, bookingInfo);
		String infusionSoftApiName = env.getInfusionSoftApiName();
		String infusionSoftApiKey = env.getInfusionSoftApiKey();
		try {
			contactService.update(infusionSoftApiName, infusionSoftApiKey,
					new AddDataQuery().withRecordID(ifContactId).withDataRecord(updateRecord));
			m_log.info("Update infusionsoft field successfully" + updateRecord.toString());
		} catch (InfSDKExecption e) {
			m_log.info("Updating custom field to Infusion Soft failed", e);
		}
	}

}
