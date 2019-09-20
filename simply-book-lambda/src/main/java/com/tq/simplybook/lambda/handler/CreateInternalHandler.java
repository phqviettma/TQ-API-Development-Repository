
package com.tq.simplybook.lambda.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.PatientDetail;
import com.tq.cliniko.lambda.model.Patients;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClientInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmBookingInfo;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmBookingInfoService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.common.lambda.utils.TimeUtils;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.req.EventReq;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.resp.End;
import com.tq.googlecalendar.resp.EventResp;
import com.tq.googlecalendar.resp.Start;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.googlecalendar.utils.GoogleCalendarUtil;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddDataQuery;
import com.tq.inf.query.AddNewContactQuery;
import com.tq.inf.query.ApplyTagQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.lambda.util.CustomField;
import com.tq.simplybook.lambda.util.SbmInfUtil;
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
	private ClinikoApiServiceBuilder clinikoApiServiceBuilder = null;
	private GoogleCalendarApiServiceBuilder googleApiBuilder = null;
	private CountryItemService countryItemService = null;
	private SbmBookingInfoService sbmBookingInfoService = null;
	private static final String AGENT = "sbm";
	private static final String DEFAULT_TIME_ZONE = "Australia/Sydney";

	public CreateInternalHandler(Env environtment, TokenServiceSbm tss, BookingServiceSbm bss, ContactServiceInf csi,
			ContactItemService cis, SbmClinikoSyncService scs, GoogleCalendarDbService gcs,
			SbmGoogleCalendarDbService sgcs, TokenGoogleCalendarService tcs, ClinikoSyncToSbmService csts,
			ClinikoCompanyInfoService ccis, ClinikoApiServiceBuilder apiServiceBuilder,
			GoogleCalendarApiServiceBuilder apiBuilder, CountryItemService countryService,
			SbmBookingInfoService bookingInfoService) {
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
		clinikoApiServiceBuilder = apiServiceBuilder;
		googleApiBuilder = apiBuilder;
		countryItemService = countryService;
		sbmBookingInfoService = bookingInfoService;
	}

	@Override
	public void handle(PayloadCallback payload) throws GoogleApiSDKException, SbmSDKException, ClinikoSDKExeption {
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
		// TSI-45
		storeBookingInfoToDb(bookingInfo);
		Integer contactId = addContactToInfusionsoft(bookingInfo);
		executeWithInfusionSoft(payload, bookingInfo, contactId);
		persitClientVoInDB(bookingInfo, contactId);

		boolean processed = false;
		String sbmId = bookingInfo.getEvent_id() + "-" + bookingInfo.getUnit_id();
		ClinikoSbmSync clinikoSbmSync = clinikoSyncToSbmService.load(sbmId);
		if (clinikoSbmSync != null) {
			processed = executeWithCliniko(payload, bookingInfo, clinikoSbmSync);
		}
		List<GoogleCalendarSbmSync> sbmGoogle = googleCalendarService.queryEmail(bookingInfo.getUnit_email());
		if (!sbmGoogle.isEmpty()) {
			processed = excuteWithGoogleCalendar(bookingInfo, payload, sbmGoogle.get(0).getRefreshToken(),
					sbmGoogle.get(0).getGoogleCalendarId());
		}
		if (processed) {
			m_log.info("The booking is synced to Cliniko/Google Calendar");
		} else {
			m_log.info("The booking is synced neither to Cliniko nor Google Calendar");
		}
	}

	private void storeBookingInfoToDb(BookingInfo clientBookingInfo) {
		SbmBookingInfo bookingInfo = sbmBookingInfoService.load(Long.parseLong(clientBookingInfo.getId()));
		if (bookingInfo == null) {
			String appointmentTime = TimeUtils.buildTimeWithFormatStartToEndTime(clientBookingInfo.getStart_date_time(),
					clientBookingInfo.getEnd_date_time());
			String appointmentDate = TimeUtils.extractDateFormatDateMonth(clientBookingInfo.getStart_date_time());
			Long timeStamp = TimeUtils.convertDateTimeToLong(clientBookingInfo.getStart_date_time());
			String address = clientBookingInfo.getLocation().getAddress1() == null ? "No set"
					: clientBookingInfo.getLocation().getAddress1();
			bookingInfo = new SbmBookingInfo(Long.parseLong(clientBookingInfo.getId()),
					clientBookingInfo.getUnit_email(), clientBookingInfo.getUnit_name(), clientBookingInfo.getLocation().getAddress2(), appointmentTime,
					timeStamp, clientBookingInfo.getClient_name(), clientBookingInfo.getClient_phone(),
					clientBookingInfo.getClient_email(), appointmentDate, address);
		}
		sbmBookingInfoService.put(bookingInfo);
	}

	public boolean executeWithInfusionSoft(PayloadCallback payload, BookingInfo bookingInfo, Integer ifContactId)
			throws SbmSDKException {
		String infusionSoftApiName = env.getInfusionSoftApiName();
		String infusionSoftApiKey = env.getInfusionSoftApiKey();
		int appliedTagId = env.getInfusionSoftCreateAppliedTag();
		Map<String, String> updateRecord = CustomField.buildInfCustomField(countryItemService, env, bookingInfo);

		try {
			contactService.update(infusionSoftApiName, infusionSoftApiKey,
					new AddDataQuery().withRecordID(ifContactId).withDataRecord(updateRecord));
			m_log.info("Update infusionsoft field successfully" + updateRecord.toString());
		} catch (InfSDKExecption e) {
			m_log.info("Updating custom field to Infusion Soft failed", e);
		}

		try {
			ApplyTagQuery applyTagQuery = new ApplyTagQuery().withContactID(ifContactId).withTagID(appliedTagId);

			boolean applied = contactService.applyTag(infusionSoftApiName, infusionSoftApiKey, applyTagQuery);
			if (applied) {
				m_log.info(String.format("Applied tag with id %d under contact id %d successfully", appliedTagId,
						ifContactId));
			} else {
				m_log.info(String.format("Can not apply tag id %d under contact id: %d", appliedTagId, ifContactId));
			}
		} catch (InfSDKExecption e) {
			m_log.info("Applying Tag " + appliedTagId + " to contact Infusion Soft failed", e);
		}
		return true;
	}

	private boolean executeWithCliniko(PayloadCallback payload, BookingInfo bookingInfo, ClinikoSbmSync clinikoSbmSync)
			throws SbmSDKException, ClinikoSDKExeption {
		m_log.info("Excute Cliniko handler with clinikoId: {}, sbmId: {}", clinikoSbmSync.getClinikoId(), clinikoSbmSync.getSbmId());
		String clinikoCompanyId[] = clinikoSbmSync.getClinikoId().split("-");
		Integer practitionerId = Integer.valueOf(clinikoCompanyId[1]);
		Integer businessId = Integer.valueOf(clinikoCompanyId[0]);

		ClinikoAppointmentService clinikoApptService = clinikoApiServiceBuilder.build(clinikoSbmSync.getApiKey());
		ClinikoCompanyInfo clinikoCompanyInfo = clinikoCompanyService.load(clinikoSbmSync.getApiKey());
		if (clinikoCompanyInfo != null) {
			Patients patientInfo = clinikoApptService.getPatient(bookingInfo.getClient_email());
			PatientDetail patientDetail = null;
			if (patientInfo == null) {
				m_log.error("The cliniko account maybe expired. Kindly check/renew it");
				return false;
			}
			if (patientInfo.getPatients().isEmpty()) {
				String firstName = SbmInfUtil.buildFirstName(bookingInfo.getClient_name());
				String lastName = SbmInfUtil.buildLastName(bookingInfo.getClient_name());
				//If lastname is empty or blank, It should be replaced by '*' due to lastName as required field in Cliniko
				if (lastName.equals("")) {
					lastName = "*";
				}
				m_log.info(String.format("Client name %s has firstname: %s, lastname: %s", bookingInfo.getClient_name(),
						firstName, lastName));
				patientDetail = clinikoApptService.createPatient(firstName, lastName, bookingInfo.getClient_email(),
						bookingInfo.getClient_phone());
			} else {
				patientDetail = patientInfo.getPatients().get(0);
			}
			DateTimeZone timeZone = DateTimeZone.forID(DEFAULT_TIME_ZONE);
			String sbmStartTime = TimeUtils.parseTime(bookingInfo.getStart_date_time());
			String sbmEndTime = TimeUtils.parseTime(bookingInfo.getEnd_date_time());
			DateTime start_time = new DateTime(sbmStartTime, timeZone);
			DateTime clinikoStartTime = start_time.withZone(DateTimeZone.UTC);
			DateTime endTime = new DateTime(sbmEndTime, timeZone);
			DateTime clinikoEndTime = endTime.withZone(DateTimeZone.UTC);

			AppointmentInfo result = clinikoApptService.createAppointment(
					new AppointmentInfo(clinikoStartTime.toString(), clinikoEndTime.toString(), patientDetail.getId(),
							practitionerId, clinikoCompanyInfo.getAppointmentType(), businessId));
			m_log.info("Create appointment successfully" + result.toString());
			SbmCliniko sbmCliniko = new SbmCliniko(payload.getBooking_id(), result.getId(), 1,
					clinikoSbmSync.getApiKey(), AGENT);

			sbmClinikoService.put(sbmCliniko);
			m_log.info("Added to database successfully" + sbmCliniko);

		}
		m_log.info("Excute Cliniko handler has been completed");
		return true;
	}

	boolean excuteWithGoogleCalendar(BookingInfo bookingInfo, PayloadCallback payload, String refreshToken,
			String googleCalendarId) throws GoogleApiSDKException, SbmSDKException {
		m_log.info("Excute google handler with calendarId: " + googleCalendarId);
		TokenReq tokenReq = new TokenReq(env.getGoogleClientId(), env.getGoogleClientSecrets(), refreshToken);
		TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);
		GoogleCalendarApiService googleApiService = googleApiBuilder.build(tokenResp.getAccess_token());

		// GoogleTimeZone
		//GoogleCalendarSettingsInfo settingInfo = googleApiService.getSettingInfo("timezone");
		String sbmStartTime = TimeUtils.parseTime(bookingInfo.getStart_date_time());
		String sbmEndTime = TimeUtils.parseTime(bookingInfo.getEnd_date_time());
		Start start = new Start(sbmStartTime, DEFAULT_TIME_ZONE);
		End end = new End(sbmEndTime, DEFAULT_TIME_ZONE);
		String clientDescription = GoogleCalendarUtil.buildClientInfo(bookingInfo.getClient_name(),
				bookingInfo.getClient_email(), bookingInfo.getClient_phone());
		EventReq req = new EventReq(start, end, clientDescription,
				bookingInfo.getClient_name() + "" + env.getGoogleCalendarEventName());
		EventResp eventResp = googleApiService.createEvent(req, googleCalendarId);
		SbmGoogleCalendar sbmGoogleCalendarSync = new SbmGoogleCalendar(payload.getBooking_id(), eventResp.getId(),
				bookingInfo.getClient_email(), 1, "sbm");
		sbmGoogleCalendarService.put(sbmGoogleCalendarSync);
		m_log.info("Add to database successfully " + sbmGoogleCalendarSync);
		m_log.info("Excute google handler has been completed");
		return true;

	}

	protected Integer addContactToInfusionsoft(BookingInfo bookingInfo) throws SbmSDKException {
		Integer contactId = null;
		long start = System.currentTimeMillis();
		try {
			Map<String, String> dataRecord = new HashMap<>();
			dataRecord.put("Email", bookingInfo.getClient_email());
			dataRecord.put("FirstName", SbmInfUtil.buildFirstName(bookingInfo.getClient_name()));
			dataRecord.put("LastName", SbmInfUtil.buildLastName(bookingInfo.getClient_name()));
			dataRecord.put("Phone1", bookingInfo.getClient_phone());
			contactId = contactService.addWithDupCheck(env.getInfusionSoftApiName(), env.getInfusionSoftApiKey(),
					new AddNewContactQuery().withDataRecord(dataRecord));
		} catch (InfSDKExecption e) {
			m_log.info("Create contact failed " + e);
		}
		m_log.info(String.format("addINFContact()= %d ms", (System.currentTimeMillis() - start)));
		return contactId;
	}

	private ContactItem persitClientVoInDB(BookingInfo bookingInfo, Integer contactInfId) {
		// build client to save DynomaDB
		long start = System.currentTimeMillis();
		ClientInfo clientInfo = new ClientInfo().withClientId(bookingInfo.getClient_id()).withContactId(contactInfId)
				.withEmail(bookingInfo.getClient_email())
				.withFirstName(SbmInfUtil.buildFirstName(bookingInfo.getClient_name()))
				.withLastName(SbmInfUtil.buildLastName(bookingInfo.getClient_name()))
				.withPhone1(bookingInfo.getClient_phone());

		ContactItem contactItem = new ContactItem().withEmail(bookingInfo.getClient_email()) // unique
				// key
				.withContactInfo(clientInfo);
		contactItemService.put(contactItem);
		m_log.info(String.format("addDBContact()= %d ms", (System.currentTimeMillis() - start)));
		return contactItem;
	}
}
