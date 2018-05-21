
package com.tq.simplybook.lambda.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikiAppointmentServiceImpl;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
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
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddDataQuery;
import com.tq.inf.query.ApplyTagQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CreateInternalHandler implements InternalHandler {
	private Env env = null;
	private BookingServiceSbm bookingService = null;
	private TokenServiceSbm tokenService = null;
	private ContactServiceInf contactService = null;
	private ContactItemService contactItemService = null;
	private SbmClinikoSyncService sbmClinikoService = null;
	private static final Logger m_log = LoggerFactory.getLogger(CreateInternalHandler.class);
	private GoogleCalendarDbService googleCalendarService = null;
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = null;
	private TokenGoogleCalendarService tokenCalendarService = null;
	private ClinikoSyncToSbmService clinikoSyncToSbmService = null;
	private ClinikoCompanyInfoService clinikoCompanyService = null;
	private static final String AGENT = "sbm";

	public CreateInternalHandler(Env environtment, TokenServiceSbm tss, BookingServiceSbm bss, ContactServiceInf csi,
			ContactItemService cis, SbmClinikoSyncService scs, GoogleCalendarDbService gcs,
			SbmGoogleCalendarDbService sgcs, TokenGoogleCalendarService tcs, ClinikoSyncToSbmService csts,
			ClinikoCompanyInfoService ccis) {
		env = environtment;
		tokenService = tss;
		bookingService = bss;
		contactService = csi;
		contactItemService = cis;
		sbmClinikoService = scs;
		googleCalendarService = gcs;
		sbmGoogleCalendarService = sgcs;
		tokenCalendarService = tcs;
		clinikoSyncToSbmService = csts;
		clinikoCompanyService = ccis;
	}

	@Override
	public void handle(PayloadCallback payload) throws SbmSDKException, ClinikoSDKExeption, GoogleApiSDKException {
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
		String clientEmail = bookingInfo.getClient_email();
		ContactItem contactItem = contactItemService.load(clientEmail);

		if (contactItem != null && contactItem.getClient() != null && contactItem.getClient().getContactId() != null) {
			executeWithInfusionSoft(payload, bookingInfo, contactItem);
		} else {
			m_log.info("There is no contact on Infusion Soft asociated to the email: " + clientEmail);
		}
		boolean processed = false;
		String sbmId = bookingInfo.getEvent_id() + "-" + bookingInfo.getUnit_id();
		ClinikoSbmSync clinikoSbmSync = clinikoSyncToSbmService.load(sbmId);
		if (clinikoSbmSync != null) {
			processed = executeWithCliniko(payload, bookingInfo, clinikoSbmSync);
		}
		GoogleCalendarSbmSync sbmGoogle = googleCalendarService.load(sbmId);
		if (sbmGoogle != null) {
			processed = excuteWithGoogleCalendar(bookingInfo, payload, sbmGoogle.getRefreshToken(),
					sbmGoogle.getGoogleCalendarId());
		}
		if (processed) {
			m_log.info("The booking is synced to Cliniko/Google Calendar");
		} else {
			m_log.info("The booking is synced neither to Cliniko nor Google Calendar");
		}
	}

	public void executeWithInfusionSoft(PayloadCallback payload, BookingInfo bookingInfo, ContactItem contactItem)
			throws SbmSDKException {

		String infusionSoftApiName = env.getInfusionSoftApiName();
		String infusionSoftApiKey = env.getInfusionSoftApiKey();
		String infusionSoftAppointmentTimeField = env.getInfusionSoftAppointmentTimeField();
		String infusionSoftAppointmentLocationField = env.getInfusionSoftAppointmentLocationField();
		String infusionSoftServiceProviderField = env.getInfusionSoftServiceProviderField();
		String infusionSoftAppointmentInstructionField = env.getInfusionSoftAppointmentInstructionField();
		String infusionSoftAppointmentDateField = env.getInfusionftAppointmentDate();
		String infusionSoftPractitionerLastName = env.getInfusionsoftPractitionerLastName();
		String infusionsoftPractitionerFirstName = env.getInfusionsoftPractitionerFirstName();
		int appliedTagId = env.getInfusionSoftCreateAppliedTag();

		Integer ifContactId = contactItem.getClient().getContactId();
		Map<String, String> updateRecord = new HashMap<>();

		updateRecord.put(infusionSoftAppointmentTimeField,
				buildApppointmentTime(bookingInfo.getStart_date_time(), bookingInfo.getEnd_date_time()));
		updateRecord.put(infusionSoftAppointmentLocationField,
				bookingInfo.getLocation() == null ? "" : String.valueOf(bookingInfo.getLocation().getTitle()));
		updateRecord.put(infusionSoftServiceProviderField, bookingInfo.getUnit_name());
		updateRecord.put(infusionSoftAppointmentInstructionField, bookingInfo.getUnit_description());
		updateRecord.put(infusionSoftAppointmentDateField, buildAppointmentDate(bookingInfo.getStart_date_time()));
		updateRecord.put(infusionsoftPractitionerFirstName, buildFirstName(bookingInfo.getUnit_name()));
		updateRecord.put(infusionSoftPractitionerLastName, buildLastName(bookingInfo.getUnit_name()));
		try {

			contactService.update(infusionSoftApiName, infusionSoftApiKey,
					new AddDataQuery().withRecordID(ifContactId).withDataRecord(updateRecord));
			m_log.info("Update infusionsoft field successfully" + updateRecord.toString());
		} catch (InfSDKExecption e) {
			throw new SbmSDKException("Updating custom field to Infusion Soft failed", e);
		}

		try {
			ApplyTagQuery applyTagQuery = new ApplyTagQuery().withContactID(ifContactId).withTagID(appliedTagId);

			contactService.appyTag(infusionSoftApiName, infusionSoftApiKey, applyTagQuery);
			m_log.info("Applied Infusionsoft Tag successfully");
		} catch (InfSDKExecption e) {
			throw new SbmSDKException("Applying Tag " + appliedTagId + " to contact Infusion Soft failed", e);
		}
	}

	private boolean executeWithCliniko(PayloadCallback payload, BookingInfo bookingInfo, ClinikoSbmSync clinikoSbmSync)
			throws SbmSDKException, ClinikoSDKExeption {
		String clinikoCompanyId[] = clinikoSbmSync.getClinikoId().split("-");
		Integer practitionerId = Integer.valueOf(clinikoCompanyId[1]);
		Integer businessId = Integer.valueOf(clinikoCompanyId[0]);
		ClinikoCompanyInfo clinikoCompanyInfo = clinikoCompanyService.load(businessId);
		if (clinikoCompanyInfo != null) {

			ClinikiAppointmentServiceImpl clinikoApptService = new ClinikiAppointmentServiceImpl(
					clinikoSbmSync.getApiKey());
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
			AppointmentInfo result = clinikoApptService.createAppointment(new AppointmentInfo(
					clinikoStartTime.toString(), clinikoEndTime.toString(), clinikoCompanyInfo.getPatientId(),
					practitionerId, clinikoCompanyInfo.getAppointmentType(), businessId));
			m_log.info("Create appointment successfully" + result.toString());
			SbmCliniko sbmCliniko = new SbmCliniko(payload.getBooking_id(), result.getId(), 1,
					clinikoSbmSync.getApiKey(), AGENT);

			sbmClinikoService.put(sbmCliniko);
			m_log.info("Added to database successfully");
		}

		return true;
	}

	private boolean excuteWithGoogleCalendar(BookingInfo bookingInfo, PayloadCallback payload, String refreshToken,
			String googleCalendarId) throws GoogleApiSDKException, SbmSDKException {

		TokenReq tokenReq = new TokenReq(env.getGoogleClientId(), env.getGoogleClientSecrets(), refreshToken);
		TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);
		GoogleCalendarApiService googleApiService = new GoogleCalendarApiServiceImpl(tokenResp.getAccess_token());

		// GoogleTimeZone
		GoogleCalendarSettingsInfo settingInfo = googleApiService.getSettingInfo("timezone");
		String sbmStartTime = TimeUtils.parseTime(bookingInfo.getStart_date_time());
		String sbmEndTime = TimeUtils.parseTime(bookingInfo.getEnd_date_time());
		Start start = new Start(sbmStartTime, settingInfo.getValue());
		End end = new End(sbmEndTime, settingInfo.getValue());
		List<Attendees> attendees = new ArrayList<>();
		attendees.add(new Attendees(bookingInfo.getClient_email(), bookingInfo.getClient_name()));
		EventReq req = new EventReq(start, end, bookingInfo.getUnit_description(), attendees,
				env.getGoogleCalendarEventName());
		EventResp eventResp = googleApiService.createEvent(req, googleCalendarId);
		m_log.info("Create event successfully with value " + eventResp.toString());
		SbmGoogleCalendar sbmGoogleCalendarSync = new SbmGoogleCalendar(payload.getBooking_id(), eventResp.getId(),
				bookingInfo.getClient_email(), 1, "sbm");
		sbmGoogleCalendarService.put(sbmGoogleCalendarSync);
		m_log.info("Add to database successfully " + sbmGoogleCalendarSync);

		return true;

	}

	private static String buildApppointmentTime(String start_date_time, String end_date_time) {
		String startTime = TimeUtils.extractTimeSbm(start_date_time);
		String endTime = TimeUtils.extractTimeSbm(end_date_time);
		return startTime + ((endTime == null || endTime.isEmpty() ? "" : " - " + endTime));
	}

	private static String buildAppointmentDate(String start_date_time) {
		String startDate = TimeUtils.extractDateSbm(start_date_time);

		return startDate == null ? "" : startDate;
	}

	private static String buildFirstName(String name) {
		String[] splitName = name.split("\\s+");
		return splitName[0];
	}

	private static String buildLastName(String name) {
		List<String> lastName = Arrays.asList(name.trim().split("\\s+"));
		if (lastName.size() > 1) {
			return lastName.get(1);
		} else {
			return "";
		}
	}

}
