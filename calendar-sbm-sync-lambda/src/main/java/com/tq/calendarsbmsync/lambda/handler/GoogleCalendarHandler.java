package com.tq.calendarsbmsync.lambda.handler;

import java.io.IOException;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tq.calendarsbmsync.lambda.model.SyncMessage;
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

public class GoogleCalendarHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static final Logger m_log = LoggerFactory.getLogger(GoogleCalendarHandler.class);
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private static int STATUS_CODE = 200;
	private Env m_env = null;
	private SpecialdayServiceSbm specialDayService = null;
	private SbmBreakTimeManagement sbmTimeManagement = null;
	private AmazonDynamoDB m_amazonDynamoDB = null;
	private GoogleCalendarDbService googleCalendarService = null;
	private TokenServiceSbm tokenService = null;
	private ContactServiceInf contactInfService = null;
	private ContactItemService contactItemService = null;
	private SbmGoogleCalendarDbService sbmCalendarService = null;
	private BookingServiceSbm bookingService = null;
	private GoogleCalendarInternalHandler createEventHandler = null;
	private GoogleCalendarInternalHandler deleteEventHandler = null;
	private TokenGoogleCalendarService tokenCalendarService = new TokenGoogleCalendarImpl();
	private SbmUnitService unitService = null;
	private GoogleCalendarModifiedSyncService modifiedChannelService = null;

	public GoogleCalendarHandler() {
		this.m_env = Env.load();
		this.specialDayService = new SpecialdayServiceSbmImpl();
		this.sbmTimeManagement = new SbmBreakTimeManagement();
		this.m_amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(m_env.getRegions(), m_env.getAwsAccessKeyId(),
				m_env.getAwsSecretAccessKey());
		this.modifiedChannelService = new GoogleCalendarModifiedSyncServiceImpl(
				new GoogleCalendarModifiedSynDaoImpl(m_amazonDynamoDB));
		this.sbmCalendarService = new SbmGoogleCalendarServiceImpl(new SbmGoogleCalendarSyncDaoImpl(m_amazonDynamoDB));
		this.googleCalendarService = new GoogleCalendarServiceImpl(new GoogleCalendarDaoImpl(m_amazonDynamoDB));
		this.tokenService = new TokenServiceImpl();
		this.contactInfService = new ContactServiceImpl();
		this.contactItemService = new ContactItemServiceImpl(new ContactItemDaoImpl(m_amazonDynamoDB));
		this.unitService = new SbmUnitServiceImpl();
		this.createEventHandler = new CreateGoogleCalendarEventHandler(m_env, tokenService, specialDayService,
				sbmTimeManagement, sbmCalendarService, unitService);
		this.bookingService = new BookingServiceSbmImpl();
		this.deleteEventHandler = new DeleteGoogleCalendarEventHandler(m_env, tokenService, googleCalendarService,
				specialDayService, sbmTimeManagement, contactItemService, contactInfService, sbmCalendarService,
				bookingService, unitService);
	}

	// for testing only
	GoogleCalendarHandler(Env env, AmazonDynamoDB db, GoogleCalendarDbService googleCalendarService,
			SpecialdayServiceSbm specialDayService, CreateGoogleCalendarEventHandler createHanler,
			DeleteGoogleCalendarEventHandler deleteHandler, SbmUnitService unitService,
			GoogleCalendarModifiedSyncService modifiedChannelService) {
		this.m_amazonDynamoDB = db;
		this.m_env = env;
		this.googleCalendarService = googleCalendarService;
		this.specialDayService = specialDayService;
		this.createEventHandler = createHanler;
		this.deleteEventHandler = deleteHandler;
		this.unitService = unitService;
		this.modifiedChannelService = modifiedChannelService;
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		m_log.info("Received notification with body " + input.getBody());
		SyncMessage info = getSyncMessageInfo(input.getBody());

		if (info != null) {
			String googleState = info.getGoogleResourceState();
			String googleChannelId = info.getGoogleChannelId();

			try {
				GoogleCalendarSbmSync googleCalendarSbmSync = googleCalendarService.load(googleChannelId);
				if (googleCalendarSbmSync != null) {
					String googleCalendarId = googleCalendarSbmSync.getGoogleCalendarId();
					TokenReq tokenReq = new TokenReq(m_env.getGoogleClientId(), m_env.getGoogleClientSecrets(),
							googleCalendarSbmSync.getRefreshToken());
					TokenResp token = tokenCalendarService.getToken(tokenReq);
					GoogleCalendarApiService googleApiService = new GoogleCalendarApiServiceImpl(
							token.getAccess_token());
					Integer maxResult = m_env.getGoogleCalendarMaxResult();
					if ("sync".equals(googleState) || "exists".equals(googleState)) {
						List<Items> confirmedItems = new ArrayList<>();
						List<Items> cancelledItems = new ArrayList<>();
						CalendarEvents eventList = null;
						String nextPageToken = googleCalendarSbmSync.getNextPageToken();
						String lastQueryTimeMin = googleCalendarSbmSync.getLastQueryTimeMin();
						String updateTime = null;
						if (lastQueryTimeMin == null) {
							String currentTime = TimeUtils.getPreviousTime();
							GoogleCalendarSettingsInfo settingInfo = googleApiService.getSettingInfo("timezone");
							updateTime = TimeUtils.getTimeFullOffset(currentTime, settingInfo.getValue());
							eventList = googleApiService.queryNewestEvent(maxResult, updateTime, googleCalendarId);
						} else {
							if (nextPageToken != null) {

								eventList = googleApiService.getUpdatedEventWithPageToken(maxResult, lastQueryTimeMin,
										nextPageToken, googleCalendarId);
							} else {
								throw new TrueQuitBadRequest(
										"Illegal state, LastQueryTimeMin is set while NextPageToken is unset");
							}
						}

						m_log.info("Fetched Google Events: " + eventList);
						Iterator<Items> item = eventList.getItems().iterator();
						int index = 0;
						while (item.hasNext() && index < 5) {
							Items eventItem = item.next();
							if ("confirmed".equals(eventItem.getStatus())) {
								confirmedItems.add(eventItem);
							} else if ("cancelled".equals(eventItem.getStatus())) {
								cancelledItems.add(eventItem);
							}
							index++;
						}

						if (!confirmedItems.isEmpty()) {
							createEventHandler.handle(confirmedItems, googleCalendarSbmSync.getSbmId());
						}
						if (!cancelledItems.isEmpty()) {

							deleteEventHandler.handle(cancelledItems, googleCalendarSbmSync.getSbmId());

						}

						GCModifiedChannel modifiedItem = modifiedChannelService
								.getChannel(googleCalendarSbmSync.getSbmId(), info.getGoogleChannelId());
						if (modifiedItem != null) {
							if (modifiedItem.getCheckStatus() == 0 || modifiedItem.getCheckStatus() == -1) {
								long timeStamp = Calendar.getInstance().getTimeInMillis();
								modifiedItem = new GCModifiedChannel(googleCalendarSbmSync.getSbmId(), 1, timeStamp,
										info.getGoogleChannelId(), googleCalendarSbmSync.getEmail());
								modifiedChannelService.put(modifiedItem);
								m_log.info("Save to modified Table successfully" + modifiedItem);
							} else {
								// do nothing
							}
						}
					}

					else {
						m_log.info("Not handle this");
					}
				} else {
					m_log.info(
							"Channel Id " + info.getGoogleChannelId() + " is not in service, ignored the notification");
				}
			} catch (Exception e) {
				resp.setStatusCode(500);
				resp.setBody(buildErrorResponseBody());
				m_log.info("Error occur", e);

			}

		}
		resp.setBody(buildSuccessResponseBody());
		resp.setStatusCode(STATUS_CODE);
		return resp;

	}

	private static String buildSuccessResponseBody() {
		ObjectNode on = jsonMapper.createObjectNode();
		on.put("succeeded", true);
		return on.toString();
	}

	private static String buildErrorResponseBody() {
		ObjectNode on = jsonMapper.createObjectNode();
		on.put("succeeded", false);
		return on.toString();
	}

	public SyncMessage getSyncMessageInfo(String value) {
		SyncMessage message = null;
		try {
			message = jsonMapper.readValue(value, SyncMessage.class);
		} catch (IOException e) {
			m_log.error("Error during parsing {} : {} .", value, e);
		}
		return message;
	}

}
