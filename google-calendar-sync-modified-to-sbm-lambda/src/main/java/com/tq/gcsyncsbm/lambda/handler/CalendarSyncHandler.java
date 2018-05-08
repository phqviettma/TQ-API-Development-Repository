package com.tq.gcsyncsbm.lambda.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tq.common.lambda.dynamodb.dao.ContactItemDaoImpl;
import com.tq.common.lambda.dynamodb.dao.GoogleCalendarDaoImpl;
import com.tq.common.lambda.dynamodb.dao.GoogleCalendarModifiedSynDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmGoogleCalendarSyncDaoImpl;
import com.tq.common.lambda.dynamodb.impl.ContactItemServiceImpl;
import com.tq.common.lambda.dynamodb.impl.GoogleCalendarModifiedSyncServiceImpl;
import com.tq.common.lambda.dynamodb.impl.GoogleCalendarServiceImpl;
import com.tq.common.lambda.dynamodb.impl.SbmGoogleCalendarServiceImpl;
import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.common.lambda.exception.TrueQuitBadRequest;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.common.lambda.utils.TimeUtils;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceImpl;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.resp.CalendarEvents;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.resp.Items;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.impl.SbmUnitServiceImpl;
import com.tq.simplybook.impl.SpecialdayServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CalendarSyncHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static final Logger m_log = LoggerFactory.getLogger(CalendarSyncHandler.class);
	private static int STATUS_CODE = 200;
	private Env m_env = null;
	private SpecialdayServiceSbm specialDayService = null;
	private SbmBreakTimeManagement sbmTimeManagement = null;
	private AmazonDynamoDB m_amazonDynamoDB = null;
	private GoogleCalendarDbService googleCalendarService = null;
	private TokenServiceSbm tokenService = null;
	private TokenGoogleCalendarService tokenCalendarService = new TokenGoogleCalendarImpl();
	private SbmUnitService unitService = null;
	private ContactServiceInf contactInfService = null;
	private ContactItemService contactItemService = null;
	private GoogleCalendarModifiedSyncService modifiedChannelService = null;
	private GCInternalHandler createEventHandler = null;
	private GCInternalHandler deleteEventHandler = null;
	private SbmGoogleCalendarDbService sbmCalendarService = null;
	private BookingServiceSbm bookingService = null;
	private static final String GC_TIME_MIN ="timeMin";

	public CalendarSyncHandler() {
		this.m_env = Env.load();
		this.specialDayService = new SpecialdayServiceSbmImpl();
		this.sbmTimeManagement = new SbmBreakTimeManagement();
		this.m_amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(m_env.getRegions(), m_env.getAwsAccessKeyId(),
				m_env.getAwsSecretAccessKey());
		this.contactInfService = new ContactServiceImpl();
		this.modifiedChannelService = new GoogleCalendarModifiedSyncServiceImpl(
				new GoogleCalendarModifiedSynDaoImpl(m_amazonDynamoDB));
		this.contactItemService = new ContactItemServiceImpl(new ContactItemDaoImpl(m_amazonDynamoDB));
		this.bookingService = new BookingServiceSbmImpl();
		this.sbmCalendarService = new SbmGoogleCalendarServiceImpl(new SbmGoogleCalendarSyncDaoImpl(m_amazonDynamoDB));
		this.modifiedChannelService = new GoogleCalendarModifiedSyncServiceImpl(
				new GoogleCalendarModifiedSynDaoImpl(m_amazonDynamoDB));
		this.unitService = new SbmUnitServiceImpl();
		this.tokenService = new TokenServiceImpl();
		this.googleCalendarService = new GoogleCalendarServiceImpl(new GoogleCalendarDaoImpl(m_amazonDynamoDB));
		this.createEventHandler = new CreateGoogleEventHandler(m_env, tokenService, specialDayService,
				sbmTimeManagement, sbmCalendarService, unitService);
		this.deleteEventHandler = new DeleteGoogleEventHandler(m_env, tokenService, googleCalendarService,
				specialDayService, sbmTimeManagement, contactItemService, contactInfService, sbmCalendarService,
				bookingService, unitService);
	}

	// for testing only
	CalendarSyncHandler(Env env, AmazonDynamoDB db, GoogleCalendarDbService googleCalendarService,
			SpecialdayServiceSbm specialDayService, CreateGoogleEventHandler createHandler,
			DeleteGoogleEventHandler deleteHandler, SbmUnitService unitService,
			GoogleCalendarModifiedSyncService modifiedChannelService, SbmGoogleCalendarDbService sbmCalendarService) {
		this.m_amazonDynamoDB = db;
		this.m_env = env;
		this.googleCalendarService = googleCalendarService;
		this.specialDayService = specialDayService;
		this.createEventHandler = createHandler;
		this.deleteEventHandler = deleteHandler;
		this.unitService = unitService;
		this.modifiedChannelService = modifiedChannelService;
		this.sbmCalendarService = sbmCalendarService;
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		boolean errorOccured = false;
		m_log.info("Start GoogleCalendar-SBM synchronization lambda");
		List<GCModifiedChannel> modifiedItems = modifiedChannelService.queryItem();
		Iterator<GCModifiedChannel> it = modifiedItems.iterator();
		int processedChannelNum = 0;
		int processedEventNum = 0;
		long timeStamp = Calendar.getInstance().getTimeInMillis();

		try {
			while (it.hasNext() && processedChannelNum < m_env.getNumberRecordDB()
					&& processedEventNum < m_env.getNumberEvent()) {
				GCModifiedChannel modifiedItem = it.next();
				GoogleCalendarSbmSync googleCalendarSbmSync = googleCalendarService.load(modifiedItem.getChannelId());
				if (googleCalendarSbmSync != null) {
					TokenReq tokenReq = new TokenReq(m_env.getGoogleClientId(), m_env.getGoogleClientSecrets(),
							googleCalendarSbmSync.getRefreshToken());
					TokenResp token = tokenCalendarService.getToken(tokenReq);
					GoogleCalendarApiService googleApiService = new GoogleCalendarApiServiceImpl(
							token.getAccess_token());
					List<Items> confirmedItems = new ArrayList<>();
					List<Items> cancelledItems = new ArrayList<>();
					CalendarEvents eventList = null;
					String nextSyncToken = googleCalendarSbmSync.getNextSyncToken();
					String nextPageToken = googleCalendarSbmSync.getNextPageToken();
					String lastQueryTimeMin = googleCalendarSbmSync.getLastQueryTimeMin();
					String sbmId = googleCalendarSbmSync.getSbmId();
					
					Integer maxResult = 0;
					maxResult = m_env.getNumberEvent() - processedEventNum;
					
					boolean timeMinQueried = false;
					String timeMin = null;
					
					if ("-BLANK-".equals(nextSyncToken)) {
						if (lastQueryTimeMin == null) {
							String currentTime = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
									.format(Calendar.getInstance().getTime());
							GoogleCalendarSettingsInfo settingInfo = googleApiService.getSettingInfo("timezone");
							timeMin = TimeUtils.getTimeFullOffset(currentTime, settingInfo.getValue());
							timeMinQueried = true;
							eventList = googleApiService.getEventWithoutToken(maxResult,GC_TIME_MIN, timeMin);
						} else {
							if (!"-BLANK-".equals(nextPageToken)) {
								eventList = googleApiService.getEventAtLastTime(maxResult,GC_TIME_MIN, lastQueryTimeMin,
 										nextPageToken);
							} else {
								throw new TrueQuitBadRequest(
										"Illegal state, LastQueryTimeMin is set while NextPageToken is unset");
							}
						}
					} else {
						if ("-BLANK-".equals(nextPageToken)) {
							eventList = googleApiService.getEventWithNextSyncToken(maxResult, nextSyncToken);
						} else {
							eventList = googleApiService.getEventWithNextPageToken(maxResult, nextSyncToken,
									nextPageToken);
						}
					}
					m_log.info("Fetched Google Events: " + eventList);

					for (Items item : eventList.getItems()) {

						if ("confirmed".equals(item.getStatus())) {
							confirmedItems.add(item);
						} else if ("cancelled".equals(item.getStatus())) {
							cancelledItems.add(item);
						}

					}

					if (!confirmedItems.isEmpty()) {
						createEventHandler.handle(confirmedItems, sbmId);
					}
					if (!cancelledItems.isEmpty()) {
						deleteEventHandler.handle(cancelledItems, sbmId);
					}

					String newNextPageToken = eventList.getNextPageToken();
					String newNextSyncToken = eventList.getNextSyncToken();
					
					boolean isDbChanged = false;
					boolean allEventsSynced = false;
					
					if (newNextPageToken == null) {
						isDbChanged = true;
						allEventsSynced = true;
						googleCalendarSbmSync.setNextPageToken("-BLANK-");
					} else {
						isDbChanged = true;
						googleCalendarSbmSync.setNextPageToken(newNextPageToken);
					}

					if (newNextSyncToken != null) {
						/*
						 * In case of newNextPageToken disappearing
						 */
						isDbChanged = true;
						googleCalendarSbmSync.setNextSyncToken(newNextSyncToken);

					} else {
						if (timeMinQueried == true) {
							googleCalendarSbmSync.setLastQueryTimeMin(timeMin);
						}
					}
					
					if (allEventsSynced) {

						modifiedItem.setTimestamp(timeStamp);
						modifiedItem.setCheckStatus(0);
						modifiedChannelService.put(modifiedItem);
					} else {
						modifiedItem.setTimestamp(timeStamp);
						modifiedItem.setCheckStatus(1);
						modifiedChannelService.put(modifiedItem);

					}

					if (isDbChanged) {
						googleCalendarService.put(googleCalendarSbmSync);
					}

					processedChannelNum++;
					processedEventNum += eventList.getItems().size();

				}

				else {
					m_log.info("Not handle this");
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

}
