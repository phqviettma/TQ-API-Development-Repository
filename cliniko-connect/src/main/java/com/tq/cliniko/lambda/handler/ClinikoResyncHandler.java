package com.tq.cliniko.lambda.handler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.context.ClinikoEnv;
import com.tq.cliniko.lambda.exception.ClinikoConnectException;
import com.tq.cliniko.lambda.model.ClinikoPractitionerConnectReq;
import com.tq.cliniko.lambda.resp.ClinikoConnectStatusResponse;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.ClinikoItemService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.GetBookingReq;
import com.tq.simplybook.resp.GetBookingResp;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class ClinikoResyncHandler implements ConnectHandler {

	private static final Logger m_log = LoggerFactory.getLogger(ClinikoResyncHandler.class);
	private ClinikoItemService m_clinikoItemService = null;
	private ClinikoSyncToSbmService m_clinikoSyncService = null;
	private BookingServiceSbm m_bookingService;
	private SbmListBookingService m_sbmBookingDBService;
	private ClinikoEnv m_eVariables;
	private TokenServiceSbm m_tokenServiceSbm;
	private SbmSyncFutureBookingsService m_sbmSyncFutureBookingService;
	private static final String BOOKING_TYPE = "non_cancelled";
	private static final String ORDER_BY = "start_date";
	
	public ClinikoResyncHandler(ClinikoSyncToSbmService clinikoSyncService, ClinikoItemService clinikoItemService, BookingServiceSbm bookingService,
			SbmListBookingService sbmBookingDBService, ClinikoEnv eVariables, TokenServiceSbm tokenServiceSbm, SbmSyncFutureBookingsService sbmSyncFutureBookingService) {
		m_clinikoSyncService = clinikoSyncService;
		m_clinikoItemService = clinikoItemService;
		m_bookingService = bookingService;
		m_sbmBookingDBService = sbmBookingDBService;
		m_eVariables = eVariables;
		m_tokenServiceSbm = tokenServiceSbm;
		m_sbmSyncFutureBookingService = sbmSyncFutureBookingService;
	}
	@Override
	public ClinikoConnectStatusResponse handle(ClinikoPractitionerConnectReq req)
			throws SbmSDKException, ClinikoSDKExeption {
		String email = req.getParams().getPractitionerEmail();
		ClinikoSbmSync clinikoSbmSync = m_clinikoSyncService.queryEmail(email);
		if (clinikoSbmSync == null) {
			throw new ClinikoConnectException("The practitioner email "+email+" does not exist") ;
		}

		String apiKey = clinikoSbmSync.getApiKey();
		m_log.info("Setting the re-sync process in the next sync for apiKey: {}", apiKey);
		ClinikoSyncStatus clinikoSyncStatus = m_clinikoItemService.queryWithIndex(apiKey);
		
		if (clinikoSyncStatus != null) {
			clinikoSyncStatus.setLatestTime(null);
			clinikoSyncStatus.setReSync(true);
			m_clinikoItemService.put(clinikoSyncStatus);
		} else {
			throw new ClinikoConnectException("The provided API Key does not exist");
		}
		
		String sbmId = clinikoSbmSync.getSbmId();
		saveFutureBookingToDB(sbmId);
		
		SbmSyncFutureBookings sbmFutureBooking = m_sbmSyncFutureBookingService.load(sbmId);
		sbmFutureBooking.setSyncStatus(1);
		m_sbmSyncFutureBookingService.put(sbmFutureBooking);
		
		m_log.info("Setting up the resync process is completed");
		ClinikoConnectStatusResponse response = new ClinikoConnectStatusResponse();
		response.setSucceeded(true);
		return response;
	}

	private void saveFutureBookingToDB(String sbmId) throws SbmSDKException {
		String eventId = sbmId.split("-")[0];
		String unitId = sbmId.split("-")[1];
		String companyLogin = m_eVariables.getSimplyBookCompanyLogin();
		String user = m_eVariables.getSimplyBookUser();
		String password = m_eVariables.getSimplyBookPassword();
		String loginEndPoint = m_eVariables.getSimplyBookServiceUrlLogin();
		String endpoint = m_eVariables.getSimplyBookAdminServiceUrl();
		String token = m_tokenServiceSbm.getUserToken(companyLogin, user, password, loginEndPoint);
		
		String dateFrom = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		GetBookingReq getBookingReq = new GetBookingReq(dateFrom, BOOKING_TYPE, ORDER_BY, Integer.valueOf(unitId),
				Integer.valueOf(eventId));
		List<GetBookingResp> bookingList = m_bookingService.getBookings(companyLogin, endpoint, token, getBookingReq);
		SbmBookingList sbmBookingItem = new SbmBookingList(sbmId, bookingList);
		m_sbmBookingDBService.put(sbmBookingItem);
		m_log.info("Added to database successfully " + sbmBookingItem.toString());
	}
}
