package com.tq.simplybook.lambda.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.service.ClinikoAppointmentService;
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
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CancelInternalHandler implements InternalHandler {
	private Env env = null;
	private ContactServiceInf contactService = null;
	private BookingServiceSbm bookingService = null;
	private TokenServiceSbm tokenService = null;
	private LatestClinikoApptServiceWrapper latestApptService = null;
	private com.tq.common.lambda.dynamodb.service.ContactItemService contactItemService = null;
	private SbmClinikoSyncService sbmClinikoService = null;
	private ClinikoAppointmentService clinikoAppointmentService = null;

	public CancelInternalHandler(Env m_env, TokenServiceSbm tss, BookingServiceSbm bss, ContactServiceInf csi,
			ContactItemService cis, SbmClinikoSyncService scs, LatestClinikoApptServiceWrapper lcsw, ClinikoAppointmentService cas) {
		env = m_env;
		tokenService = tss;
		bookingService = bss;
		contactService = csi;
		contactItemService = cis;
		latestApptService = lcsw;
		sbmClinikoService = scs;
		clinikoAppointmentService = cas;
	}

	@Override
	public void handle(PayloadCallback payload) throws SbmSDKException, ClinikoSDKExeption {
		executeWithCliniko(executeWithInfusionSoft(payload));
	}

	private PayloadCallback executeWithInfusionSoft(PayloadCallback payload) throws SbmSDKException {
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

		return payload;
	}

	private void executeWithCliniko(PayloadCallback payload) throws SbmSDKException, ClinikoSDKExeption {
		SbmCliniko sbmCliniko = sbmClinikoService.load(payload.getBooking_id());
	
		LatestClinikoAppts latestClinikoAppts = latestApptService.load();
		Set<Long> createdId = latestClinikoAppts.getCreated();
		createdId.remove(sbmCliniko.getClinikoId());
		Set<Long> removeId = latestClinikoAppts.getRemoved();
		removeId.add(sbmCliniko.getClinikoId());
		Date date = new Date();
		latestClinikoAppts.setCreated(createdId);
		latestClinikoAppts.setRemoved(removeId);
		latestClinikoAppts.setLatestUpdateTime(Config.DATE_FORMAT_24_H.format(date));
		latestApptService.put(latestClinikoAppts);
		clinikoAppointmentService.deleteAppointment(sbmCliniko.getClinikoId());
		
		

	}
}
