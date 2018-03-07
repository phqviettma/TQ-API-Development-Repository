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
import com.tq.common.lambda.dynamodb.impl.ContactItemServiceImpl;
import com.tq.common.lambda.dynamodb.impl.GoogleCalendarServiceImpl;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.exception.TrueQuitBadRequest;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.lambda.context.Env;
import com.tq.googlecalendar.lambda.exception.GoogleExceptionHanlder;
import com.tq.googlecalendar.lambda.model.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.lambda.model.GoogleConnectFailureResponse;
import com.tq.googlecalendar.lambda.model.GoogleConnectStatusResponse;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.impl.SbmUnitServiceImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.TokenServiceSbm;

public class GoogleHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static int STATUS_CODE = 200;
	private static final String CHECK = "check";
	private static final String CONNECT = "connect";
	private static final String DISCONNECT = "disconnect";
	private static final Logger m_log = LoggerFactory.getLogger(GoogleHandler.class);
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private SbmUnitService sbmUnitService = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private Env eVariables = null;
	private AmazonDynamoDB amazonDynamoDB = null;
	private GoogleCalendarDbService googleCalendarService = null;
	private ContactServiceInf contactService = null;
	private ContactItemService contactItemService = null;
	private GoogleCalendarApiServiceBuilder apiServiceBuilder = null;
	private TokenGoogleCalendarService tokenCalendarService = new TokenGoogleCalendarImpl();
	private Handler connectHandler = null;
	private Handler disconnectHandler = null;
	private Handler checkStatusHandler = null;


	public GoogleHandler() {

		this.sbmUnitService = new SbmUnitServiceImpl();
		this.tokenServiceSbm = new TokenServiceImpl();
		this.eVariables = Env.load();
		this.amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(eVariables.getRegions(), eVariables.getAwsAccessKeyId(),
				eVariables.getAwsSecretAccessKey());
		;
		this.googleCalendarService = new GoogleCalendarServiceImpl(new GoogleCalendarDaoImpl(amazonDynamoDB));
		this.contactService = new ContactServiceImpl();
		this.tokenCalendarService = new TokenGoogleCalendarImpl();
		this.apiServiceBuilder = new GoogleCalendarApiServiceBuilder();
		this.contactItemService = new ContactItemServiceImpl(new ContactItemDaoImpl(amazonDynamoDB));
		this.connectHandler = new GoogleConnectCalendarHandler(eVariables, googleCalendarService, contactService,
				contactItemService, tokenCalendarService, sbmUnitService, tokenServiceSbm,apiServiceBuilder);
		this.disconnectHandler = new GoogleDisconnectCalendarHandler(eVariables, googleCalendarService,
				tokenCalendarService, apiServiceBuilder);
		this.checkStatusHandler = new GoogleCalendarCheckStatusHandler(googleCalendarService);

	}

	// for testing only
	GoogleHandler(Env env, AmazonDynamoDB db, SbmUnitService unitService, TokenServiceSbm tokenService,
			GoogleCalendarDbService calendarService, ContactServiceInf contactService,
			ContactItemService contactItemService, GoogleConnectCalendarHandler connectHandler,
			GoogleCalendarCheckStatusHandler checkHandler, GoogleDisconnectCalendarHandler disconnectHandler) {
		this.amazonDynamoDB = db;
		this.sbmUnitService = unitService;
		this.tokenServiceSbm = tokenService;
		this.googleCalendarService = calendarService;
		this.eVariables = env;
		this.contactService = contactService;
		this.contactItemService = contactItemService;
		this.connectHandler = connectHandler;
		this.checkStatusHandler = checkHandler;
		this.disconnectHandler = disconnectHandler;
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
			String jsonResp = GoogleExceptionHanlder.buildFailMessageResponse(failResponse.isSucceeded(),failResponse.getErrorMessage());
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
			e.printStackTrace();
		}
		return jsonResp;

	}

	
}
