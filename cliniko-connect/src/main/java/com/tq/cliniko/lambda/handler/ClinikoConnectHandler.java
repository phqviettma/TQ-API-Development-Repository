package com.tq.cliniko.lambda.handler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.context.ClinikoEnv;
import com.tq.cliniko.lambda.exception.ClinikoConnectException;
import com.tq.cliniko.lambda.model.ClinikoPractitionerConnectReq;
import com.tq.cliniko.lambda.resp.ClinikoConnectStatusResponse;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoItemService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.GetBookingReq;
import com.tq.simplybook.resp.GetBookingResp;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.TokenServiceSbm;

public class ClinikoConnectHandler implements ConnectHandler {
	private ClinikoEnv eVariables = null;
	private static final Logger m_log = LoggerFactory.getLogger(ClinikoConnectHandler.class);
	private SbmUnitService unitServiceSbm = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private ClinikoSyncToSbmService clinikoSyncService = null;
	private ClinikoItemService clinikoItemService = null;
	private ClinikoCompanyInfoService clinikoCompanyService = null;
	private BookingServiceSbm bookingService = null;
	private static final String BOOKING_TYPE = "non_cancelled";
	private static final String ORDER_BY = "start_date";
	private SbmSyncFutureBookingsService sbmSyncFutureBookingService = null;
	private SbmListBookingService sbmBookingDBService = null;

	public ClinikoConnectHandler(ClinikoEnv env, SbmUnitService unitService, TokenServiceSbm tokenService,
			ClinikoSyncToSbmService clinikoSyncService, ClinikoItemService clinikoItemService,
			ClinikoCompanyInfoService clinikoCompanyService, SbmSyncFutureBookingsService sbmSyncFutureBookingService,
			BookingServiceSbm bookingService, SbmListBookingService sbmBookingDBService) {
		this.eVariables = env;
		this.unitServiceSbm = unitService;
		this.tokenServiceSbm = tokenService;
		this.clinikoSyncService = clinikoSyncService;
		this.clinikoItemService = clinikoItemService;
		this.clinikoCompanyService = clinikoCompanyService;
		this.sbmSyncFutureBookingService = sbmSyncFutureBookingService;
		this.bookingService = bookingService;
		this.sbmBookingDBService = sbmBookingDBService;
	}

	@Override
	public ClinikoConnectStatusResponse handle(ClinikoPractitionerConnectReq req)
			throws SbmSDKException, ClinikoSDKExeption {
		ClinikoConnectStatusResponse response = new ClinikoConnectStatusResponse();
		String companyLogin = eVariables.getSimplyBookCompanyLogin();
		String user = eVariables.getSimplyBookUser();
		String password = eVariables.getSimplyBookPassword();
		String loginEndPoint = eVariables.getSimplyBookServiceUrlLogin();
		String endpoint = eVariables.getSimplyBookAdminServiceUrl();
		String token = tokenServiceSbm.getUserToken(companyLogin, user, password, loginEndPoint);
		List<UnitProviderInfo> unitInfos = unitServiceSbm.getUnitList(companyLogin, endpoint, token, true, true, 1);
		String apiKey = req.getParams().getApiKey();
		ClinikoSbmSync clinikoSbmSync = clinikoSyncService.queryWithIndex(apiKey);
		if (clinikoSbmSync == null) {
			String email = req.getParams().getPractitionerEmail();
			boolean done = false;
			for (UnitProviderInfo unitInfo : unitInfos) {
				if (unitInfo.getEmail() != null && unitInfo.getEmail().equals(email)) {
					if (unitInfo.getEvent_map() != null) {
						Set<String> eventInfos = unitInfo.getEvent_map().keySet();
						Iterator<String> it = eventInfos.iterator();
						if (it.hasNext()) {
							String eventId = it.next();
							String unitId = unitInfo.getId();
							String sbmId = eventId + "-" + unitId;
							String clinikoId = req.getParams().getBusinessId() + "-" + req.getParams().getPractitionerId();
							ClinikoCompanyInfo clinikoCompanyInfo = new ClinikoCompanyInfo(req.getParams().getBusinessId(), req.getParams().getAppointmentTypeId(), apiKey);
							saveToDBWhenPracConnect(req, companyLogin, endpoint, token, apiKey, eventId, unitId,
									sbmId, clinikoId, clinikoCompanyInfo);
							done = true;
							break;
						}
					} else {
						m_log.info("Please assiociate a service for service provider in SBM");
                        throw new ClinikoConnectException(
                                "Please associate a service for service provider in SBM, service provider details: name " + unitInfo.getName()
                                        + ", email " + unitInfo.getEmail());
					}
				}
			}

			if (!done) {
				throw new ClinikoConnectException(
						"There is no Cliniko practitioner associated with the provided API Key " + apiKey);
			}

		} else {
			throw new ClinikoConnectException("This practitioner has already connected");
		}
		response.setSucceeded(true);
		return response;
	}

	private void saveToDBWhenPracConnect(ClinikoPractitionerConnectReq req, String companyLogin, String endpoint,
			String token, String apiKey, String eventId, String unitId, String sbmId, String clinikoId, ClinikoCompanyInfo clinikoCompanyInfo)
			throws ClinikoSDKExeption, SbmSDKException {
		ClinikoSbmSync clinikoSbmSync;
		clinikoSbmSync = new ClinikoSbmSync(apiKey, req.getParams().getPractitionerEmail(), clinikoId, sbmId);
		clinikoSyncService.put(clinikoSbmSync);
		long timeStamp = Calendar.getInstance().getTimeInMillis();
		ClinikoSyncStatus clinikoItem = new ClinikoSyncStatus();
		clinikoItem.setApiKey(apiKey);
		clinikoItem.setTimeStamp(timeStamp);
		clinikoItemService.put(clinikoItem);
		clinikoCompanyService.put(clinikoCompanyInfo);
		SbmSyncFutureBookings sbmSyncFutureBookingItem = new SbmSyncFutureBookings(sbmId, apiKey, 1, timeStamp);
		sbmSyncFutureBookingService.put(sbmSyncFutureBookingItem);
		String dateFrom = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		GetBookingReq getBookingReq = new GetBookingReq(dateFrom, BOOKING_TYPE, ORDER_BY, Integer.valueOf(unitId),
				Integer.valueOf(eventId));
		List<GetBookingResp> bookingList = bookingService.getBookings(companyLogin, endpoint, token, getBookingReq);
		SbmBookingList sbmBookingItem = new SbmBookingList(sbmId, bookingList);
		sbmBookingDBService.put(sbmBookingItem);
	}
}
