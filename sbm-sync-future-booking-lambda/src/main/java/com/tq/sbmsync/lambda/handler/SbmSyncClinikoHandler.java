package com.tq.sbmsync.lambda.handler;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceWrapper;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppts;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.response.LambdaStatusResponse;
import com.tq.common.lambda.utils.TimeUtils;
import com.tq.sbmsync.lambda.model.SbmSyncReq;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.req.GetBookingReq;
import com.tq.simplybook.resp.GetBookingResp;
import com.tq.simplybook.service.TokenServiceSbm;

public class SbmSyncClinikoHandler implements SbmInternalHandler {
	private ClinikoSyncToSbmService clinikoSyncDBService = null;
	private BookingServiceSbmImpl bookingSbmService = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private Env eVariables = null;
	private static final Logger m_log = LoggerFactory.getLogger(SbmSyncClinikoHandler.class);
	private static final String BOOKING_TYPE = "non_cancelled";
	private static final String ORDER_BY = "start_date";
	private SbmClinikoSyncService sbmClinikoService = null;
	private LatestClinikoApptServiceWrapper latestApptService = null;
	private ClinikoAppointmentService clinikoApptService = null;

	@Override
	public LambdaStatusResponse handle(SbmSyncReq req) throws SbmSDKException, ClinikoSDKExeption {
		ClinikoSbmSync clinikoSbmSync = clinikoSyncDBService.load(req.getParams().getClinikoApiKey());
		LambdaStatusResponse response = new LambdaStatusResponse();
		if (clinikoSbmSync != null) {
			Integer unitId = clinikoSbmSync.getUnitId();
			Integer serviceId = clinikoSbmSync.getServiceId();
			String clinikoId[] = clinikoSbmSync.getClinikoId().split("-");
			Integer bussinessId = Integer.valueOf(clinikoId[0]);
			Integer practitionerId = Integer.valueOf(clinikoId[1]);
			String companyLogin = eVariables.getSimplyBookCompanyLogin();
			String user = eVariables.getSimplyBookUser();
			String password = eVariables.getSimplyBookPassword();
			String loginEndPoint = eVariables.getSimplyBookServiceUrlLogin();
			String endpoint = eVariables.getSimplyBookAdminServiceUrl();
			String token = tokenServiceSbm.getUserToken(companyLogin, user, password, loginEndPoint);
			String dateFrom = Calendar.getInstance().toString();

			GetBookingReq getBookingReq = new GetBookingReq(dateFrom, BOOKING_TYPE, ORDER_BY, unitId, serviceId);
			List<GetBookingResp> bookingList = bookingSbmService.getBookings(companyLogin, endpoint, token,
					getBookingReq);
			Iterator<GetBookingResp> booking = bookingList.iterator();
			while (booking.hasNext()) {
				GetBookingResp bookingResp = booking.next();
				Settings settings = clinikoApptService.getAllSettings();
				String country = settings.getAccount().getCountry();
				String time_zone = settings.getAccount().getTime_zone();
				DateTimeZone timeZone = DateTimeZone.forID(country + "/" + time_zone);
				String sbmStartTime = TimeUtils.parseTime(bookingResp.getStart_date());
				String sbmEndTime = TimeUtils.parseTime(bookingResp.getEnd_date());
				DateTime start_time = new DateTime(sbmStartTime, timeZone);
				DateTime clinikoStartTime = start_time.withZone(DateTimeZone.UTC);
				DateTime endTime = new DateTime(sbmEndTime, timeZone);
				DateTime clinikoEndTime = endTime.withZone(DateTimeZone.UTC);
				AppointmentInfo result = clinikoApptService.createAppointment(new AppointmentInfo(
						clinikoStartTime.toString(), clinikoEndTime.toString(), eVariables.getClinikoPatientId(),
						practitionerId, eVariables.getCliniko_standard_appointment(), bussinessId));
				m_log.info("Create appointment successfully" + result.toString());
				SbmCliniko sbmCliniko = new SbmCliniko();
				sbmCliniko.setClinikoId(result.getId());
				sbmCliniko.setSbmId(Long.parseLong(bookingResp.getId()));

				LatestClinikoAppts latestClinikoAppts = latestApptService.load();
				Set<Long> createdId = latestClinikoAppts.getCreated();
				createdId.add(result.getId());
				latestClinikoAppts.setCreated(createdId);
				String latestTime = TimeUtils.getNowInUTC(country + "/" + time_zone);
				latestClinikoAppts.setLatestUpdateTime(latestTime);
				m_log.info("LatestClinikoAppt" + latestClinikoAppts);
				if (sbmCliniko != null) {
					sbmClinikoService.put(sbmCliniko);
					latestApptService.put(latestClinikoAppts);
					m_log.info("Added to database successfully");
				}
			}
		}
		response.setSucceeded(true);
		return response;
	}

}
