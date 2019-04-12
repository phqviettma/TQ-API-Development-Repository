package com.tq.sbmsync.lambda.handler;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.common.lambda.dynamodb.dao.ClinikoCompanyInfoDaoImpl;
import com.tq.common.lambda.dynamodb.dao.ClinikoSyncToSbmDaoImpl;
import com.tq.common.lambda.dynamodb.dao.GoogleCalendarDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmClinikoSyncDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmGoogleCalendarSyncDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmListBookingDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmSyncFutureBookingDaoImpl;
import com.tq.common.lambda.dynamodb.impl.ClinikoCompanyInfoServiceImpl;
import com.tq.common.lambda.dynamodb.impl.ClinikoSyncToSbmServiceImpl;
import com.tq.common.lambda.dynamodb.impl.GoogleCalendarServiceImpl;
import com.tq.common.lambda.dynamodb.impl.SbmClinikoSyncImpl;
import com.tq.common.lambda.dynamodb.impl.SbmGoogleCalendarServiceImpl;
import com.tq.common.lambda.dynamodb.impl.SbmListBookingServiceImpl;
import com.tq.common.lambda.dynamodb.impl.SbmSyncFutureBookingServiceImpl;
import com.tq.common.lambda.dynamodb.model.SbmBookingInfo;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmBookingInfoService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.common.lambda.utils.TimeUtils;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.GetBookingResp;

public class SbmSyncHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static int STATUS_CODE = 200;
	private static final Logger m_log = LoggerFactory.getLogger(SbmSyncHandler.class);
	private ClinikoSyncToSbmService clinikoSyncService = null;
	private Env env = null;
	private SbmClinikoSyncService sbmClinikoSyncService = null;
	private AmazonDynamoDB m_amazonDynamoDB = null;
	private SbmInternalHandler sbmSyncGCHandler = null;
	private SbmInternalHandler sbmSyncClinikoHandler = null;
	private GoogleCalendarDbService googleCalendarDbService = null;
	private TokenGoogleCalendarService tokenCalendarService = null;
	private ClinikoCompanyInfoService clinikoCompanyService = null;
	private SbmSyncFutureBookingsService sbmSyncFutureBookingService = null;
	private SbmListBookingService sbmListBookingService = null;
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = null;
	private GoogleCalendarApiServiceBuilder apiServiceBuilder = null;
	private ClinikoApiServiceBuilder clinikoApiServiceBuilder = null;
	private SbmBookingInfoService sbmBookingService = null;

	public SbmSyncHandler() {
		this.env = Env.load();

		this.m_amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(env.getRegions(), env.getAwsAccessKeyId(),
				env.getAwsSecretAccessKey());
		this.sbmClinikoSyncService = new SbmClinikoSyncImpl(new SbmClinikoSyncDaoImpl(m_amazonDynamoDB));
		this.clinikoSyncService = new ClinikoSyncToSbmServiceImpl(new ClinikoSyncToSbmDaoImpl(m_amazonDynamoDB));
		this.googleCalendarDbService = new GoogleCalendarServiceImpl(new GoogleCalendarDaoImpl(m_amazonDynamoDB));
		this.tokenCalendarService = new TokenGoogleCalendarImpl();
		this.sbmSyncFutureBookingService = new SbmSyncFutureBookingServiceImpl(
				new SbmSyncFutureBookingDaoImpl(m_amazonDynamoDB));
		this.clinikoApiServiceBuilder = new ClinikoApiServiceBuilder();
		this.sbmGoogleCalendarService = new SbmGoogleCalendarServiceImpl(
				new SbmGoogleCalendarSyncDaoImpl(m_amazonDynamoDB));
		this.apiServiceBuilder = new GoogleCalendarApiServiceBuilder();
		this.sbmListBookingService = new SbmListBookingServiceImpl(new SbmListBookingDaoImpl(m_amazonDynamoDB));
		this.clinikoCompanyService = new ClinikoCompanyInfoServiceImpl(new ClinikoCompanyInfoDaoImpl(m_amazonDynamoDB));
		this.sbmSyncClinikoHandler = new SbmSyncClinikoHandler(clinikoSyncService, sbmClinikoSyncService,
				clinikoCompanyService, sbmSyncFutureBookingService, clinikoApiServiceBuilder, sbmListBookingService);
		this.sbmSyncGCHandler = new SbmSyncGCHandler(googleCalendarDbService, env, tokenCalendarService,
				sbmSyncFutureBookingService, sbmListBookingService, sbmGoogleCalendarService, apiServiceBuilder);
	}

	// for testing only
	SbmSyncHandler(Env env, ClinikoSyncToSbmService clinikoSyncService, SbmClinikoSyncService sbmClinikoSyncService,
			GoogleCalendarDbService googleCalendarDbService, TokenGoogleCalendarService tokenCalendarService,
			SbmSyncClinikoHandler clinikoHandler, SbmSyncGCHandler gcHandler,
			SbmSyncFutureBookingsService sbmSyncFutureBookingService, SbmListBookingService sbmListBookingService, SbmBookingInfoService sbmBookingService) {
		this.env = env;
		this.sbmClinikoSyncService = sbmClinikoSyncService;
		this.googleCalendarDbService = googleCalendarDbService;
		this.tokenCalendarService = tokenCalendarService;
		this.sbmSyncGCHandler = gcHandler;
		this.sbmSyncClinikoHandler = clinikoHandler;
		this.sbmSyncFutureBookingService = sbmSyncFutureBookingService;
		this.sbmListBookingService = sbmListBookingService;
		this.sbmBookingService = sbmBookingService;

	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		m_log.info("Start Lambda");
		boolean errorOccured = false;
		List<SbmSyncFutureBookings> sbmSyncFutureBookingItems = sbmSyncFutureBookingService.querySyncStatus();
		try {
			if (!sbmSyncFutureBookingItems.isEmpty()) {
				Iterator<SbmSyncFutureBookings> sbmItem = sbmSyncFutureBookingItems.iterator();
				while (sbmItem.hasNext()) {
					SbmSyncFutureBookings sbmSyncItem = sbmItem.next();
					saveBookingInfoToDb(sbmSyncItem.getSbmId(), sbmSyncItem.getEmail());
					if (sbmSyncItem.getClinikoApiKey() != null) {
						sbmSyncClinikoHandler.handle(sbmSyncItem);
					} else if (sbmSyncItem.getEmail() != null) {
						sbmSyncGCHandler.handle(sbmSyncItem);
					} else {
						throw new SbmSDKException("Error, can not sync to cliniko/google");
					}
				}
			}
		} catch (Exception e) {
			m_log.error("Error occurs", e);
			resp.setStatusCode(500);
			errorOccured = true;
		}
		if (!errorOccured) {
			resp.setStatusCode(STATUS_CODE);
		}
		return resp;
	}

	private void saveBookingInfoToDb(String sbmId, String practitionerEmail) {
		SbmBookingList bookingLists = sbmListBookingService.load(sbmId);
		if (bookingLists != null && !bookingLists.getBookingList().isEmpty()) {
			for (GetBookingResp bookingInfo : bookingLists.getBookingList()) {
				String apptTime = TimeUtils.buildTimeWithFormatStartToEndTime(bookingInfo.getStart_date(),
						bookingInfo.getEnd_date());
				String apptDate = TimeUtils.extractDateFormatDateMonth(bookingInfo.getStart_date());
				Long timeStamp = TimeUtils.convertDateTimeToLong(bookingInfo.getStart_date());
				SbmBookingInfo sbmBookingInfo = new SbmBookingInfo(Long.parseLong(bookingInfo.getId()),
						practitionerEmail, bookingInfo.getClient(),
						bookingInfo.getClient_email(), apptTime, apptDate, timeStamp, bookingInfo.getUnitName());
				sbmBookingService.put(sbmBookingInfo);
			}
		}

	}
}
