package com.tq.simplybook.lambda.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.req.TokenReq;
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

public class CancelInternalHandler implements InternalHandler {
	private Env env = null;
	private static final Logger m_log = LoggerFactory.getLogger(CancelInternalHandler.class);
	private ContactServiceInf contactService = null;
	private BookingServiceSbm bookingService = null;
	private TokenServiceSbm tokenService = null;
	private ContactItemService contactItemService = null;
	private SbmClinikoSyncService sbmClinikoService = null;
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = null;
	private GoogleCalendarDbService googleCalendarService = null;
	private TokenGoogleCalendarService tokenCalendarService = null;
	private ClinikoSyncToSbmService clinikoSyncToSbmService = null;
	private ClinikoApiServiceBuilder clinikoApiServiceBuilder = null;
	private GoogleCalendarApiServiceBuilder googleApiServiceBuilder = null;

	public CancelInternalHandler(Env m_env, TokenServiceSbm tss, BookingServiceSbm bss, ContactServiceInf csi,
			ContactItemService cis, SbmClinikoSyncService scs, SbmGoogleCalendarDbService sgcs,
			GoogleCalendarDbService gcs, TokenGoogleCalendarService tcs, ClinikoSyncToSbmService csts,
			ClinikoApiServiceBuilder clinikoApiServiceBuilder,
			GoogleCalendarApiServiceBuilder googleApiServiceBuilder) {
		env = m_env;
		tokenService = tss;
		bookingService = bss;
		contactService = csi;
		contactItemService = cis;
		sbmClinikoService = scs;
		sbmGoogleCalendarService = sgcs;
		googleCalendarService = gcs;
		tokenCalendarService = tcs;
		this.clinikoSyncToSbmService = csts;
		this.clinikoApiServiceBuilder = clinikoApiServiceBuilder;
		this.googleApiServiceBuilder = googleApiServiceBuilder;
	}

	@Override
	public void handle(PayloadCallback payload) throws SbmSDKException, ClinikoSDKExeption, GoogleApiSDKException {
		BookingInfo bookingInfo = executeWithInfusionSoft(payload);
		boolean processed = false;
		String sbmId = bookingInfo.getEvent_id() + "-" + bookingInfo.getUnit_id();
		ClinikoSbmSync clinikoSbmSync = clinikoSyncToSbmService.load(sbmId);
		if (clinikoSbmSync != null) {

			processed = executeWithCliniko(payload, clinikoSbmSync.getApiKey());
		}
		List<GoogleCalendarSbmSync> sbmGoogle = googleCalendarService.queryEmail(bookingInfo.getUnit_email());
		if (!sbmGoogle.isEmpty()) {
			processed = excuteWithGoogleCalendar(sbmGoogle.get(0).getRefreshToken(), payload,
					sbmGoogle.get(0).getGoogleCalendarId());
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

		int appliedTagId = env.getInfusionSoftCancelAppliedTag();
		Long bookingId = payload.getBooking_id();

		String token = tokenService.getUserToken(companyLogin, user, password, loginEndPoint);

		BookingInfo bookingInfo = bookingService.getBookingInfo(companyLogin, adminEndPoint, token, bookingId);

		if (bookingInfo == null) {
			m_log.info("There is no booking content asociated to the booking id: " + bookingId);
		}

		// load with email as id from DynamoDB
		String clientEmail = bookingInfo.getClient_email();

		// get
		ContactItem contactItem = contactItemService.load(clientEmail);

		if (contactItem == null || contactItem.getClient() == null || contactItem.getClient().getContactId() == null) {
			m_log.info("There is no contact on Infusion Soft asociated to the email: " + clientEmail);
		} else {
			Integer ifContactId = contactItem.getClient().getContactId();

			Map<String, String> updateRecord = resetRecordCustomFiledIS();

			try {
				contactService.update(infusionSoftApiName, infusionSoftApiKey,
						new AddDataQuery().withRecordID(ifContactId).withDataRecord(updateRecord));

			} catch (InfSDKExecption e) {
				m_log.info("Updating custom field to Infusion Soft failed " + e);
			}

			try {
				ApplyTagQuery applyTagQuery = new ApplyTagQuery().withContactID(ifContactId).withTagID(appliedTagId);

				boolean isApplied = contactService.applyTag(infusionSoftApiName, infusionSoftApiKey, applyTagQuery);
				if (isApplied) {
					m_log.info(String.format("Applied tag with id %d under contact id %d successfully", appliedTagId,
							ifContactId));
				} else {
					m_log.info(
							String.format("Can not apply tag id %d under contact id: %d", appliedTagId, ifContactId));
				}
			} catch (InfSDKExecption e) {
				m_log.info("Applying Tag " + appliedTagId + " to contact Infusion Soft failed" + e);
			}
		}
		return bookingInfo;
	}

	public boolean executeWithCliniko(PayloadCallback payload, String apiKey)
			throws SbmSDKException, ClinikoSDKExeption {
		ClinikoAppointmentService clinikoAppointmentService = clinikoApiServiceBuilder.build(apiKey);
		SbmCliniko sbmCliniko = sbmClinikoService.load(payload.getBooking_id());
		if (sbmCliniko != null) {
			clinikoAppointmentService.deleteAppointment(sbmCliniko.getClinikoId());
			sbmCliniko.setFlag(0);
			sbmClinikoService.put(sbmCliniko);
			m_log.info("Update to database successfully " + sbmCliniko);
		}
		return true;

	}

	public boolean excuteWithGoogleCalendar(String refreshToken, PayloadCallback payload, String googleCalendarId)
			throws SbmSDKException, GoogleApiSDKException {
		TokenReq tokenReq = new TokenReq(env.getGoogleClientId(), env.getGoogleClientSecrets(), refreshToken);

		TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);
		GoogleCalendarApiService googleService = googleApiServiceBuilder.build(tokenResp.getAccess_token());
		SbmGoogleCalendar sbmGoogleCalendar = sbmGoogleCalendarService.load(payload.getBooking_id());
		if (sbmGoogleCalendar == null) {
			return false;
		} else {
			sbmGoogleCalendar.setFlag(0);
			sbmGoogleCalendarService.put(sbmGoogleCalendar);
			googleService.deleteEvent(sbmGoogleCalendar.getEventId(), googleCalendarId);
			m_log.info("Delete google event successfully");
			return true;
		}
	}

	public Map<String, String> resetRecordCustomFiledIS() {
		String infusionSoftAppointmentTimeField = env.getInfusionSoftAppointmentTimeField();
		String infusionSoftAppointmentLocationField = env.getInfusionSoftAppointmentLocationField();
		String infusionSoftServiceProviderField = env.getInfusionSoftServiceProviderField();
		String infusionSoftAppointmentInstructionField = env.getInfusionSoftAppointmentInstructionField();
		String infusionSoftAppointmentDateField = env.getInfusionftAppointmentDate();
		String infusionSoftPractitionerLastName = env.getInfusionsoftPractitionerLastName();
		String infusionsoftPractitionerFirstName = env.getInfusionsoftPractitionerFirstName();
		String infusionsoftAppointmentAddress1 = env.getInfusionsoftApptAddress1();
		String infusionsoftAppointmentAddress2 = env.getInfusionsoftApptAddress2();
		String infusionsoftAppointmentCity = env.getInfusionsoftApptCity();
		String infusionsoftAppointmentCountry = env.getInfusionsoftApptCountry();
		String infusionsoftAppointmentPhone = env.getInfusionsoftApptPhone();
		String infusionsoftAppointmentZip = env.getInfusionsoftApptZip();
		Map<String, String> updateRecord = new HashMap<>();
		updateRecord.put(infusionSoftAppointmentTimeField, "");
		updateRecord.put(infusionSoftAppointmentLocationField, "");
		updateRecord.put(infusionSoftServiceProviderField, "");
		updateRecord.put(infusionSoftAppointmentInstructionField, "");
		updateRecord.put(infusionSoftAppointmentDateField, "");
		updateRecord.put(infusionSoftPractitionerLastName, "");
		updateRecord.put(infusionsoftPractitionerFirstName, "");
		updateRecord.put(infusionsoftAppointmentAddress1, "");
		updateRecord.put(infusionsoftAppointmentAddress2, "");
		updateRecord.put(infusionsoftAppointmentCity, "");
		updateRecord.put(infusionsoftAppointmentCountry, "");
		updateRecord.put(infusionsoftAppointmentPhone, "");
		updateRecord.put(infusionsoftAppointmentZip, "");
		return updateRecord;
	}
}
