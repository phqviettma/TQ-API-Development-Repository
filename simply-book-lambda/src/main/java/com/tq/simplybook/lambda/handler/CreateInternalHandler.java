
package com.tq.simplybook.lambda.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikiAppointmentServiceImpl;
import com.tq.cliniko.lambda.model.Account;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.cliniko.time.UtcTimeUtil;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceWrapper;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppts;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.LatestClinikoApptService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddDataQuery;
import com.tq.inf.query.ApplyTagQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.lambda.context.Env;
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.resp.ClinikoId;
import com.tq.simplybook.resp.SimplyBookId;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CreateInternalHandler implements InternalHandler {
	private Env env = null;
	private BookingServiceSbm bookingService = null;
	private TokenServiceSbm tokenService = null;
	private ContactServiceInf contactService = null;
	private ContactItemService contactItemService = null;
	private SbmClinikoSyncService sbmClinikoService = null;
	private LatestClinikoApptServiceWrapper latestApptService = null;
	private ClinikoAppointmentService clinikoApptService = null;

	private SimplyBookClinikoMapping sbmClinikoMapping = null;

	public CreateInternalHandler(Env environtment, TokenServiceSbm tss, BookingServiceSbm bss, ContactServiceInf csi,
			ContactItemService cis, SimplyBookClinikoMapping scm, SbmClinikoSyncService scs,
			LatestClinikoApptServiceWrapper lcsw, ClinikoAppointmentService cas) {
		env = environtment;
		tokenService = tss;
		bookingService = bss;
		contactService = csi;
		contactItemService = cis;
		sbmClinikoMapping = scm;
		sbmClinikoService = scs;
		latestApptService = lcsw;
		clinikoApptService = cas;

	}

	@Override
	public void handle(PayloadCallback payload) throws SbmSDKException, ClinikoSDKExeption {
		executeWithCliniko(payload, executeWithInfusionSoft(payload));
	}

	public BookingInfo executeWithInfusionSoft(PayloadCallback payload) throws SbmSDKException {
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

		int appliedTagId = env.getInfusionSoftCreateAppliedTag();

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

		updateRecord.put(infusionSoftAppointmentTimeField,
				buildApppointmentTime(bookingInfo.getStart_date_time(), bookingInfo.getEnd_date_time()));
		updateRecord.put(infusionSoftAppointmentLocationField,
				bookingInfo.getLocation() == null ? "" : String.valueOf(bookingInfo.getLocation().getTitle()));
		updateRecord.put(infusionSoftServiceProviderField, bookingInfo.getUnit_name());
		updateRecord.put(infusionSoftAppointmentInstructionField, bookingInfo.getUnit_description());

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

	private void executeWithCliniko(PayloadCallback payload, BookingInfo bookingInfo)
			throws SbmSDKException, ClinikoSDKExeption {

		String clinikoApiKey = env.getClinikoApiKey();
		Integer appointmentTypeId = env.getCliniko_standard_appointment();
		Integer clinikoPatientId = env.getClinikoPatientId();
		Settings settings = clinikoApptService.getAllSettings();
		String country = settings.getAccount().getCountry();
		String time_zone = settings.getAccount().getTime_zone();
		DateTimeZone timeZone = DateTimeZone.forID(country+"/"+time_zone);
		String sbmStartTime = UtcTimeUtil.parseTimeUTC(bookingInfo.getStart_date_time());
		String sbmEndTime = UtcTimeUtil.parseTimeUTC(bookingInfo.getEnd_date_time());
		DateTime start_time = new DateTime(sbmStartTime,timeZone);
		DateTime clinikoStartTime = start_time.withZone(DateTimeZone.UTC);
		DateTime endTime = new DateTime(sbmEndTime,timeZone);
		DateTime clinikoEndTime = endTime.withZone(DateTimeZone.UTC);
		ClinikoId clinikoId = new ClinikoId();
		SimplyBookId simplybookId = new SimplyBookId(bookingInfo.getEvent_id(), bookingInfo.getUnit_id());
		clinikoId = sbmClinikoMapping.sbmClinikoMapping(simplybookId);
		ClinikoAppointmentService clinikoService = new ClinikiAppointmentServiceImpl(clinikoApiKey);

		AppointmentInfo result = clinikoService.createAppointment(new AppointmentInfo(clinikoStartTime.toString(), clinikoEndTime.toString(),
				clinikoPatientId, clinikoId.getPractionerId(), appointmentTypeId, clinikoId.getBussinessId()));

		SbmCliniko sbmCliniko = new SbmCliniko();
		sbmCliniko.setClinikoId(result.getId());
		sbmCliniko.setSbmId(payload.getBooking_id());

		LatestClinikoAppts latestClinikoAppts = latestApptService.load();
		Set<Long> createdId = latestClinikoAppts.getCreated();
		createdId.add(result.getId());
		latestClinikoAppts.setCreated(createdId);
		Date date = new Date();
		latestClinikoAppts.setLatestUpdateTime(Config.DATE_FORMAT_24_H.format(date));
		if (sbmCliniko != null) {
			sbmClinikoService.put(sbmCliniko);
			latestApptService.put(latestClinikoAppts);
		}

	}

	private static String buildApppointmentTime(String start_date_time, String end_date_time) {
		return start_date_time + ((end_date_time == null || end_date_time.isEmpty() ? "" : " - " + end_date_time));
	}

}
