package com.tq.sbmsync.lambda.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
import com.tq.common.lambda.response.LambdaStatusResponse;
import com.tq.common.lambda.utils.TimeUtils;
import com.tq.sbmsync.lambda.model.FindNewBooking;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.GetBookingResp;

public class SbmSyncClinikoHandler implements SbmInternalHandler {
	private ClinikoSyncToSbmService clinikoSyncDBService = null;
	private static final Logger m_log = LoggerFactory.getLogger(SbmSyncClinikoHandler.class);
	private static final int syncNumber = 30;
	private static final String AGENT = "sbm";
	private SbmClinikoSyncService sbmClinikoService = null;
	private ClinikoApiServiceBuilder apiServiceBuilder = null;
	private ClinikoCompanyInfoService clinikoCompanyService = null;
	private SbmSyncFutureBookingsService sbmSyncFutureBookingService = null;
	private SbmListBookingService sbmListBookingService = null;

	public SbmSyncClinikoHandler(ClinikoSyncToSbmService clinikoSyncDBService, SbmClinikoSyncService sbmClinikoService,
			ClinikoCompanyInfoService clinikoCompanyService, SbmSyncFutureBookingsService sbmSyncFutureBookingService,
			ClinikoApiServiceBuilder apiServiceBuilder,SbmListBookingService sbmListBookingService) {
		this.clinikoSyncDBService = clinikoSyncDBService;
		this.sbmClinikoService = sbmClinikoService;
		this.clinikoCompanyService = clinikoCompanyService;
		this.sbmSyncFutureBookingService = sbmSyncFutureBookingService;
		this.apiServiceBuilder = apiServiceBuilder;
		this.sbmListBookingService = sbmListBookingService;
	}

	@Override
	public LambdaStatusResponse handle(SbmSyncFutureBookings req) throws SbmSDKException, ClinikoSDKExeption {
		ClinikoSbmSync clinikoSbmSync = clinikoSyncDBService.queryWithIndex(req.getClinikoApiKey());
		LambdaStatusResponse response = new LambdaStatusResponse();
		int processNumber = 0;
		if (clinikoSbmSync != null) {
			String clinikoId[] = clinikoSbmSync.getClinikoId().split("-");
			Integer bussinessId = Integer.valueOf(clinikoId[0]);
			Integer practitionerId = Integer.valueOf(clinikoId[1]);
			ClinikoCompanyInfo clinikoCompanyInfo = clinikoCompanyService.load(clinikoSbmSync.getApiKey());
			if (clinikoCompanyInfo != null) {
				ClinikoAppointmentService clinikoApptService = apiServiceBuilder.build(clinikoSbmSync.getApiKey());
				SbmBookingList bookingLists = sbmListBookingService.load(clinikoSbmSync.getSbmId());
				if (bookingLists != null && !bookingLists.getBookingList().isEmpty()) {
					FindNewBooking newBookings = findNewBooking(bookingLists.getBookingList());
					Iterator<GetBookingResp> booking = newBookings.getBookingList().iterator();

					while (processNumber < syncNumber && booking.hasNext()) {
						GetBookingResp bookingResp = booking.next();
						DateTimeZone timeZone = DateTimeZone.forID(TimeUtils.DEFAULT_TIME_ZONE);
						String sbmStartTime = TimeUtils.parseTime(bookingResp.getStart_date());
						String sbmEndTime = TimeUtils.parseTime(bookingResp.getEnd_date());
						DateTime start_time = new DateTime(sbmStartTime, timeZone);
						DateTime clinikoStartTime = start_time.withZone(DateTimeZone.UTC);
						DateTime endTime = new DateTime(sbmEndTime, timeZone);
						DateTime clinikoEndTime = endTime.withZone(DateTimeZone.UTC);
						Patients patients = clinikoApptService.getPatient(bookingResp.getClient_email());
						PatientDetail patientDetail = null;
						if(patients.getPatients().isEmpty()) {
							patientDetail = clinikoApptService.createPatient(bookingResp.getClient(),bookingResp.getClient(), bookingResp.getClient_email(), bookingResp.getPhone());
						}
						else {
							patientDetail = patients.getPatients().get(0);
						}
						AppointmentInfo result = clinikoApptService
								.createAppointment(new AppointmentInfo(clinikoStartTime.toString(),
										clinikoEndTime.toString(), patientDetail.getId(), practitionerId,
										clinikoCompanyInfo.getAppointmentType(), bussinessId));
						m_log.info("Create appointment successfully" + result.toString());
						SbmCliniko sbmCliniko = new SbmCliniko(Long.parseLong(bookingResp.getId()), result.getId(), 1,
								req.getClinikoApiKey(), AGENT);
						sbmClinikoService.put(sbmCliniko);
						m_log.info("Save to database successfully " + sbmCliniko);
						processNumber++;
					}
					if (newBookings.getBookingList().isEmpty()) {
						req.setSyncStatus(0);
						sbmSyncFutureBookingService.put(req);

					}

				}
				else {
					req.setSyncStatus(0);
					sbmSyncFutureBookingService.put(req);
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
				SbmCliniko sbmCliniko = sbmClinikoService.load(Long.parseLong(bookingItem.getId()));
				if (sbmCliniko == null) {
					newBooking.add(bookingItem);
					num++;
				}
			}
		}
		return new FindNewBooking(num, newBooking);
	}

}
