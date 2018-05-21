package com.tq.sbmsync.lambda.handler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikiAppointmentServiceImpl;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
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
	private static final String AGENT = "sbm";
	private SbmClinikoSyncService sbmClinikoService = null;
	private ClinikoCompanyInfoService clinikoCompanyService = null;

	public SbmSyncClinikoHandler(ClinikoSyncToSbmService clinikoSyncDBService, BookingServiceSbmImpl bookingSbmService,
			TokenServiceSbm tokenServiceSbm, Env eVariables, SbmClinikoSyncService sbmClinikoService,
			ClinikoCompanyInfoService clinikoCompanyService) {
		this.clinikoSyncDBService = clinikoSyncDBService;
		this.bookingSbmService = bookingSbmService;
		this.tokenServiceSbm = tokenServiceSbm;
		this.eVariables = eVariables;
		this.sbmClinikoService = sbmClinikoService;
		this.clinikoCompanyService = clinikoCompanyService;
	}

	@Override
	public LambdaStatusResponse handle(SbmSyncReq req) throws SbmSDKException, ClinikoSDKExeption {
		ClinikoSbmSync clinikoSbmSync = clinikoSyncDBService.queryWithIndex(req.getParams().getClinikoApiKey());
		LambdaStatusResponse response = new LambdaStatusResponse();
		if (clinikoSbmSync != null) {
			String sbmId[] = clinikoSbmSync.getSbmId().split("-");
			Integer unitId = Integer.valueOf(sbmId[1]);
			Integer eventId = Integer.valueOf(sbmId[0]);
			String clinikoId[] = clinikoSbmSync.getClinikoId().split("-");
			Integer bussinessId = Integer.valueOf(clinikoId[0]);
			Integer practitionerId = Integer.valueOf(clinikoId[1]);
			String companyLogin = eVariables.getSimplyBookCompanyLogin();
			String user = eVariables.getSimplyBookUser();
			String password = eVariables.getSimplyBookPassword();
			String loginEndPoint = eVariables.getSimplyBookServiceUrlLogin();
			String endpoint = eVariables.getSimplyBookAdminServiceUrl();
			String token = tokenServiceSbm.getUserToken(companyLogin, user, password, loginEndPoint);
			String dateFrom = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
			ClinikoCompanyInfo clinikoCompanyInfo = clinikoCompanyService.load(bussinessId);
			ClinikoAppointmentService clinikoApptService = new ClinikiAppointmentServiceImpl(
					clinikoSbmSync.getApiKey());
			GetBookingReq getBookingReq = new GetBookingReq(dateFrom, BOOKING_TYPE, ORDER_BY, unitId, eventId);
			List<GetBookingResp> bookingList = bookingSbmService.getBookings(companyLogin, endpoint, token,
					getBookingReq);
			Iterator<GetBookingResp> booking = bookingList.iterator();
			if (clinikoCompanyInfo != null) {
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
							clinikoStartTime.toString(), clinikoEndTime.toString(), clinikoCompanyInfo.getPatientId(),
							practitionerId,clinikoCompanyInfo.getAppointmentType(), bussinessId));
					m_log.info("Create appointment successfully" + result.toString());
					SbmCliniko sbmCliniko = sbmClinikoService.load(Long.parseLong(bookingResp.getId()));
					if (sbmCliniko == null) {
						sbmCliniko = new SbmCliniko(Long.parseLong(bookingResp.getId()), result.getId(), 1,
								req.getParams().getClinikoApiKey(), AGENT);
						sbmClinikoService.put(sbmCliniko);
						m_log.info("Save to database successfully " + sbmCliniko);
					}
				}
			}
		}
		response.setSucceeded(true);
		return response;
	}

}
