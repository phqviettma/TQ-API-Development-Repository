package com.tq.simplybook.lambda.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.calendar.exception.GoogleApiSDKException;
import com.tq.calendar.impl.GoogleCalendarApiServiceImpl;
import com.tq.calendar.req.TokenReq;
import com.tq.calendar.resp.TokenResp;
import com.tq.calendar.service.GoogleCalendarApiService;
import com.tq.calendar.service.TokenGoogleCalendarService;
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.cliniko.time.UtcTimeUtil;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceWrapper;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppts;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddDataQuery;
import com.tq.inf.query.ApplyTagQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.context.SimplyBookClinikoMapping;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.resp.ClinikoId;
import com.tq.simplybook.resp.SimplyBookId;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CancelInternalHandler implements InternalHandler {
	private Env env = null;
	private static final Logger m_log = LoggerFactory.getLogger(CancelInternalHandler.class);
	private ContactServiceInf contactService = null;
	private BookingServiceSbm bookingService = null;
	private TokenServiceSbm tokenService = null;
	private LatestClinikoApptServiceWrapper latestApptService = null;
	private com.tq.common.lambda.dynamodb.service.ContactItemService contactItemService = null;
	private SbmClinikoSyncService sbmClinikoService = null;
	private ClinikoAppointmentService clinikoAppointmentService = null;
	private SimplyBookClinikoMapping sbmClinikoMapping = null;
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = null;
	private GoogleCalendarDbService googleCalendarService = null;
	private TokenGoogleCalendarService tokenCalendarService = null;

	public CancelInternalHandler(Env m_env, TokenServiceSbm tss, BookingServiceSbm bss, ContactServiceInf csi,
			ContactItemService cis, SbmClinikoSyncService scs, LatestClinikoApptServiceWrapper lcsw,
			ClinikoAppointmentService cas, SimplyBookClinikoMapping scm, SbmGoogleCalendarDbService sgcs,
			GoogleCalendarDbService gcs, TokenGoogleCalendarService tcs) {
		env = m_env;
		tokenService = tss;
		bookingService = bss;
		contactService = csi;
		contactItemService = cis;
		latestApptService = lcsw;
		sbmClinikoService = scs;
		clinikoAppointmentService = cas;
		sbmClinikoMapping = scm;
		sbmGoogleCalendarService = sgcs;
		googleCalendarService = gcs;
		tokenCalendarService = tcs;
	}

	@Override
	public void handle(PayloadCallback payload) throws SbmSDKException, ClinikoSDKExeption, GoogleApiSDKException {
		BookingInfo bookingInfo = executeWithInfusionSoft(payload);
		SimplyBookId simplybookId = new SimplyBookId(bookingInfo.getEvent_id(), bookingInfo.getUnit_id());
		ClinikoId clinikoId = sbmClinikoMapping.sbmClinikoMapping(simplybookId);
		boolean processed = false;

		if (clinikoId != null) {
			processed = executeWithCliniko(payload);
		} else {
			processed = excuteWithGoogleCalendar(simplybookId, payload);
		}

		if (processed) {
			m_log.info("The cancellation is synced to Cliniko/Google Calendar");
		} else {
			m_log.info("The cancellation is synced neither to Cliniko nor Google Calendar");
		}

	}

	private BookingInfo executeWithInfusionSoft(PayloadCallback payload) throws SbmSDKException {
		String companyLogin = env.getSimplyBookCompanyLogin();
		String user = env.getSimplyBookUser();
		String password = env.getSimplyBookPassword();
		String loginEndPoint = env.getSimplyBookServiceUrlLogin();
		String adminEndPoint = env.getSimplyBookAdminServiceUrl();
		String infusionSoftApiName = env.getInfusionSoftApiName();
		String infusionSoftApiKey = env.getInfusionSoftApiKey();

		String infusionSoftAppointmentTimeField = env.getInfusionSoftAppointmentTimeField();
		String infusionSoftAppointmentLocationField = env.getInfusionSoftAppointmentLocationField();
		String infusionSoftServiceProviderField = env.getInfusionSoftServiceProviderField();
		String infusionSoftAppointmentInstructionField = env.getInfusionSoftAppointmentInstructionField();

		int appliedTagId = env.getInfusionSoftCancelAppliedTag();
		Long bookingId = payload.getBooking_id();

		String token = tokenService.getUserToken(companyLogin, user, password, loginEndPoint);

		BookingInfo bookingInfo = bookingService.getBookingInfo(companyLogin, adminEndPoint, token, bookingId);

		if (bookingInfo == null) {
			throw new SbmSDKException("There is no booking content asociated to the booking id: " + bookingId);
		}

		// load with email as id from DynamoDB
		String clientEmail = bookingInfo.getClient_email();

		// get
		ContactItem contactItem = contactItemService.load(clientEmail);

		if (contactItem == null || contactItem.getClient() == null || contactItem.getClient().getContactId() == null) {
			throw new SbmSDKException("There is no contact on Infusion Soft asociated to the email: " + clientEmail);
		}

		Integer ifContactId = contactItem.getClient().getContactId();

		Map<String, String> updateRecord = new HashMap<>();
		updateRecord.put(infusionSoftAppointmentTimeField, "");
		updateRecord.put(infusionSoftAppointmentLocationField, "");
		updateRecord.put(infusionSoftServiceProviderField, "");
		updateRecord.put(infusionSoftAppointmentInstructionField, "");

		try {
			contactService.update(infusionSoftApiName, infusionSoftApiKey,
					new AddDataQuery().withRecordID(ifContactId).withDataRecord(updateRecord));

		} catch (InfSDKExecption e) {
			throw new SbmSDKException("Updating custom field to Infusion Soft failed", e);
		}

		try {
			ApplyTagQuery applyTagQuery = new ApplyTagQuery().withContactID(ifContactId).withTagID(appliedTagId);

			contactService.appyTag(infusionSoftApiName, infusionSoftApiKey, applyTagQuery);
		} catch (InfSDKExecption e) {
			throw new SbmSDKException("Applying Tag " + appliedTagId + " to contact Infusion Soft failed", e);
		}

		return bookingInfo;
	}

	private boolean executeWithCliniko(PayloadCallback payload) throws SbmSDKException, ClinikoSDKExeption {

		SbmCliniko sbmCliniko = sbmClinikoService.load(payload.getBooking_id());
		if (sbmCliniko == null) {
			return false;

		} else {
			LatestClinikoAppts latestClinikoAppts = latestApptService.load();
			Set<Long> createdId = latestClinikoAppts.getCreated();
			createdId.remove(sbmCliniko.getClinikoId());
			Set<Long> removeId = latestClinikoAppts.getRemoved();
			removeId.add(sbmCliniko.getClinikoId());
			latestClinikoAppts.setCreated(createdId);
			latestClinikoAppts.setRemoved(removeId);
			Settings settings = clinikoAppointmentService.getAllSettings();
			String country = settings.getAccount().getCountry();
			String time_zone = settings.getAccount().getTime_zone();
			String latestTime = UtcTimeUtil.getNowInUTC(country + "/" + time_zone);
			latestClinikoAppts.setLatestUpdateTime(latestTime);

			clinikoAppointmentService.deleteAppointment(sbmCliniko.getClinikoId());
			latestApptService.put(latestClinikoAppts);
			return true;
		}
	}

	private boolean excuteWithGoogleCalendar(SimplyBookId simplybookId, PayloadCallback payload)
			throws SbmSDKException, GoogleApiSDKException {
		String eventId = simplybookId.getEvent_id();
		String unitId = simplybookId.getUnit_id();
		String sbmId = eventId + "-" + unitId;
		GoogleCalendarSbmSync calendarSbm = googleCalendarService.load(sbmId);
		boolean processed = false;
		if (calendarSbm != null) {
			processed = true;
			TokenReq tokenReq = new TokenReq(env.getGoogleClientId(), env.getGoogleClientSecrets(),
					calendarSbm.getRefreshToken());

			TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);
			GoogleCalendarApiService googleService = new GoogleCalendarApiServiceImpl(tokenResp.getAccess_token());
			SbmGoogleCalendar sbmGoogleCalendar = sbmGoogleCalendarService.load(payload.getBooking_id());
			if (sbmGoogleCalendar == null) {
				return false;
			} else {
				sbmGoogleCalendarService.delete(sbmGoogleCalendar);
				m_log.info("Delete item on database successfully");
				googleService.deleteEvent(sbmGoogleCalendar.getEventId());
				m_log.info("Delete google event successfully");
				return true;
			}

		}

		return processed;
	}
}
