package com.tq.googlecalendar.lambda.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.common.lambda.dynamodb.dao.ContactItemDaoImpl;
import com.tq.common.lambda.dynamodb.dao.GoogleCalendarDaoImpl;
import com.tq.common.lambda.dynamodb.dao.GoogleCalendarModifiedSynDaoImpl;
import com.tq.common.lambda.dynamodb.dao.GoogleRenewChannelDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmListBookingDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmSyncFutureBookingDaoImpl;
import com.tq.common.lambda.dynamodb.impl.ContactItemServiceImpl;
import com.tq.common.lambda.dynamodb.impl.GoogleCalendarModifiedSyncServiceImpl;
import com.tq.common.lambda.dynamodb.impl.GoogleCalendarServiceImpl;
import com.tq.common.lambda.dynamodb.impl.GoogleWatchChannelDbServiceImpl;
import com.tq.common.lambda.dynamodb.impl.SbmListBookingServiceImpl;
import com.tq.common.lambda.dynamodb.impl.SbmSyncFutureBookingServiceImpl;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalRenewService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
import com.tq.common.lambda.exception.TrueQuitBadRequest;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.lambda.exception.GoogleExceptionHanlder;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.lambda.resp.GoogleConnectFailureResponse;
import com.tq.googlecalendar.lambda.resp.GoogleConnectStatusResponse;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.SbmUnitServiceImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.TokenServiceSbm;

public class GoogleHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static int STATUS_CODE = 200;
	private static final String CHECK = "check";
	private static final String CONNECT = "connect";
	private static final String DISCONNECT = "disconnect";
	private static final String SHOW_CALENDAR = "show";
	private static final Logger m_log = LoggerFactory.getLogger(GoogleHandler.class);
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private SbmUnitService sbmUnitService = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private Env eVariables = null;
	private AmazonDynamoDB amazonDynamoDB = null;
	private GoogleCalendarDbService googleCalendarService = null;
	private ContactItemService contactItemService = null;
	private GoogleCalendarApiServiceBuilder apiServiceBuilder = null;
	private GoogleCalRenewService googleWatchChannelDbService = null;
	private SbmSyncFutureBookingsService sbmSyncFutureBooking = null;
	private BookingServiceSbmImpl bookingSbmService  = null;
	private  SbmListBookingService sbmListBookingService = null;
	private TokenGoogleCalendarService tokenCalendarService = new TokenGoogleCalendarImpl();
	private GoogleCalendarModifiedSyncService calendarModifiedChannelService = null;
	private Handler connectHandler = null;
	private Handler disconnectHandler = null;
	private Handler checkStatusHandler = null;
	private Handler showCalendarHandler = null;

	public GoogleHandler() {

		this.sbmUnitService = new SbmUnitServiceImpl();
		this.tokenServiceSbm = new TokenServiceImpl();
		this.eVariables = Env.load();
		this.amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(eVariables.getRegions(), eVariables.getAwsAccessKeyId(),
				eVariables.getAwsSecretAccessKey());
		;
		this.googleCalendarService = new GoogleCalendarServiceImpl(new GoogleCalendarDaoImpl(amazonDynamoDB));
		this.calendarModifiedChannelService = new GoogleCalendarModifiedSyncServiceImpl(
				new GoogleCalendarModifiedSynDaoImpl(amazonDynamoDB));
		this.tokenCalendarService = new TokenGoogleCalendarImpl();
		this.apiServiceBuilder = new GoogleCalendarApiServiceBuilder();
		this.contactItemService = new ContactItemServiceImpl(new ContactItemDaoImpl(amazonDynamoDB));
		this.googleWatchChannelDbService = new GoogleWatchChannelDbServiceImpl(
				new GoogleRenewChannelDaoImpl(amazonDynamoDB));
		this.sbmSyncFutureBooking = new SbmSyncFutureBookingServiceImpl(
				new SbmSyncFutureBookingDaoImpl(amazonDynamoDB));
		this.bookingSbmService = new BookingServiceSbmImpl();
		this.sbmListBookingService = new SbmListBookingServiceImpl(new SbmListBookingDaoImpl(amazonDynamoDB));
		this.connectHandler = new GoogleConnectCalendarHandler(eVariables, googleCalendarService, contactItemService,
				tokenCalendarService, sbmUnitService, tokenServiceSbm, apiServiceBuilder, googleWatchChannelDbService,
				calendarModifiedChannelService, sbmSyncFutureBooking, bookingSbmService, sbmListBookingService);
		this.disconnectHandler = new GoogleDisconnectCalendarHandler(eVariables, googleCalendarService,
				tokenCalendarService, apiServiceBuilder, googleWatchChannelDbService, calendarModifiedChannelService,
				sbmSyncFutureBooking, sbmListBookingService);
		this.checkStatusHandler = new GoogleCalendarCheckStatusHandler(googleCalendarService);
		this.showCalendarHandler = new ShowGoogleCalendarHandler(tokenCalendarService, eVariables, apiServiceBuilder);

	}

	// for testing only
	GoogleHandler(Env env, AmazonDynamoDB db, SbmUnitService unitService, TokenServiceSbm tokenService,
			GoogleCalendarDbService calendarService, ContactItemService contactItemService,
			GoogleConnectCalendarHandler connectHandler, GoogleCalendarCheckStatusHandler checkHandler,
			GoogleDisconnectCalendarHandler disconnectHandler, ShowGoogleCalendarHandler showCalendarHandler) {
		this.amazonDynamoDB = db;
		this.sbmUnitService = unitService;
		this.tokenServiceSbm = tokenService;
		this.googleCalendarService = calendarService;
		this.eVariables = env;
		this.contactItemService = contactItemService;
		this.connectHandler = connectHandler;
		this.checkStatusHandler = checkHandler;
		this.disconnectHandler = disconnectHandler;
		this.showCalendarHandler = showCalendarHandler;
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		m_log.info("Received one request with body " + input.getBody());
		GoogleRegisterReq req = getRegisterReq(input.getBody());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("content-Type", "application/json");
		resp.setHeaders(headers);
		GoogleConnectStatusResponse response = new GoogleConnectStatusResponse();
		try {
			if (CHECK.equals(req.getAction())) {
				response = checkStatusHandler.handle(req);

			} else if (SHOW_CALENDAR.equals(req.getAction())) {
				response = showCalendarHandler.handle(req);
			} else if (CONNECT.equals(req.getAction())) {
				response = connectHandler.handle(req);

			} else if (DISCONNECT.equals(req.getAction())) {
				response = disconnectHandler.handle(req);

			} else {
				throw new TrueQuitBadRequest("Bad request");
			}

		} catch (Exception e) {
			m_log.error("Error occurs", e);

			GoogleConnectFailureResponse failResponse = GoogleExceptionHanlder.handle(e);
			String jsonResp = GoogleExceptionHanlder.buildFailMessageResponse(failResponse.isSucceeded(),
					failResponse.getErrorMessage());
			resp.setBody(jsonResp);
			resp.setStatusCode(failResponse.getStatusCode());
			return resp;
		}
		String jsonResp = buildResponse(response);
		resp.setBody(jsonResp);
		resp.setStatusCode(STATUS_CODE);
		m_log.info("Response " + resp.getBody());
		return resp;
	}

	public GoogleRegisterReq getRegisterReq(String value) {
		GoogleRegisterReq req = null;
		try {
			req = jsonMapper.readValue(value, GoogleRegisterReq.class);
		} catch (IOException e) {
			m_log.error("Error during parsing {} : {} .", value, e);
		}
		return req;
	}

	private static String buildResponse(GoogleConnectStatusResponse response) {
		String jsonResp = null;
		try {
			jsonResp = jsonMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			m_log.info("Error during parsing {} : {} .", response, e);
		}
		return jsonResp;

	}

}
