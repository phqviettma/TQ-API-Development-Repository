package com.tq.sbmsync.lambda.handler;

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
import com.tq.common.lambda.dynamodb.dao.ClinikoCompanyInfoDaoImpl;
import com.tq.common.lambda.dynamodb.dao.ClinikoSyncToSbmDaoImpl;
import com.tq.common.lambda.dynamodb.dao.GoogleCalendarDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmClinikoSyncDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmGoogleCalendarSyncDaoImpl;
import com.tq.common.lambda.dynamodb.impl.ClinikoCompanyInfoServiceImpl;
import com.tq.common.lambda.dynamodb.impl.ClinikoSyncToSbmServiceImpl;
import com.tq.common.lambda.dynamodb.impl.GoogleCalendarServiceImpl;
import com.tq.common.lambda.dynamodb.impl.SbmClinikoSyncImpl;
import com.tq.common.lambda.dynamodb.impl.SbmGoogleCalendarServiceImpl;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.common.lambda.exception.TrueQuitBadRequest;
import com.tq.common.lambda.response.LambdaStatusResponse;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.sbmsync.lambda.model.SbmSyncReq;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.TokenServiceSbm;

public class SbmSyncHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static int STATUS_CODE = 200;
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private static final Logger m_log = LoggerFactory.getLogger(SbmSyncHandler.class);
	private static final String CLINIKO = "cliniko";
	private static final String GOOGLE = "google";
	private ClinikoSyncToSbmService clinikoSyncService = null;
	private Env env = null;
	private BookingServiceSbmImpl bookingService = null;
	private TokenServiceSbm tokenService = null;
	private SbmClinikoSyncService sbmClinikoSyncService = null;
	private AmazonDynamoDB m_amazonDynamoDB = null;
	private SbmInternalHandler sbmSyncGCHandler = null;
	private SbmInternalHandler sbmSyncClinikoHandler = null;
	private GoogleCalendarDbService googleCalendarDbService = null;
	private TokenGoogleCalendarService tokenCalendarService = null;
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = null;
	private ClinikoCompanyInfoService clinikoCompanyService = null;

	public SbmSyncHandler() {
		this.tokenService = new TokenServiceImpl();
		this.env = Env.load();
		this.bookingService = new BookingServiceSbmImpl();
		this.m_amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(env.getRegions(), env.getAwsAccessKeyId(),
				env.getAwsSecretAccessKey());
		this.sbmClinikoSyncService = new SbmClinikoSyncImpl(new SbmClinikoSyncDaoImpl(m_amazonDynamoDB));
		this.clinikoSyncService = new ClinikoSyncToSbmServiceImpl(new ClinikoSyncToSbmDaoImpl(m_amazonDynamoDB));
		this.googleCalendarDbService = new GoogleCalendarServiceImpl(new GoogleCalendarDaoImpl(m_amazonDynamoDB));
		this.tokenCalendarService = new TokenGoogleCalendarImpl();
		this.sbmGoogleCalendarService = new SbmGoogleCalendarServiceImpl(
				new SbmGoogleCalendarSyncDaoImpl(m_amazonDynamoDB));
		this.clinikoCompanyService = new ClinikoCompanyInfoServiceImpl(new ClinikoCompanyInfoDaoImpl(m_amazonDynamoDB));
		this.sbmSyncClinikoHandler = new SbmSyncClinikoHandler(clinikoSyncService, bookingService, tokenService, env,
				sbmClinikoSyncService, clinikoCompanyService);
		this.sbmSyncGCHandler = new SbmSyncGCHandler(googleCalendarDbService, env, bookingService, tokenService,
				tokenCalendarService, sbmGoogleCalendarService);
	}

	// for testing only
	SbmSyncHandler(Env env, ClinikoSyncToSbmService clinikoSyncService, TokenServiceSbm tokenService,
			SbmClinikoSyncService sbmClinikoSyncService, GoogleCalendarDbService googleCalendarDbService,
			TokenGoogleCalendarService tokenCalendarService, SbmGoogleCalendarDbService sbmGoogleCalendarService,
			SbmSyncClinikoHandler clinikoHandler, SbmSyncGCHandler gcHandler) {
		this.env = env;
		this.tokenService = tokenService;
		this.sbmClinikoSyncService = sbmClinikoSyncService;
		this.googleCalendarDbService = googleCalendarDbService;
		this.tokenCalendarService = tokenCalendarService;
		this.sbmGoogleCalendarService = sbmGoogleCalendarService;
		this.sbmSyncGCHandler = gcHandler;
		this.sbmSyncClinikoHandler = clinikoHandler;

	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		m_log.info("Received one request with body " + input.getBody());
		SbmSyncReq req = getSbmSyncReq(input.getBody());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("content-Type", "application/json");
		resp.setHeaders(headers);
		LambdaStatusResponse response = new LambdaStatusResponse();
		try {
			if (CLINIKO.equals(req.getAgent())) {
				m_log.info("Req " + req);
				response = sbmSyncClinikoHandler.handle(req);
			} else if (GOOGLE.equals(req.getAgent())) {
				response = sbmSyncGCHandler.handle(req);
			} else {
				throw new TrueQuitBadRequest("Bad request");
			}
		} catch (Exception e) {
			m_log.error("Error occurs", e);
		}
		String jsonResp = buildResponse(response);
		resp.setBody(jsonResp);
		resp.setStatusCode(STATUS_CODE);
		m_log.info("Response " + resp.getBody());
		return resp;
	}

	public SbmSyncReq getSbmSyncReq(String value) {
		SbmSyncReq req = null;
		try {
			req = jsonMapper.readValue(value, SbmSyncReq.class);
		} catch (IOException e) {
			m_log.error("Error during parsing {} : {} .", value, e);
		}
		return req;
	}

	private static String buildResponse(LambdaStatusResponse response) {
		String jsonResp = null;
		try {
			jsonResp = jsonMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			m_log.info("Error during parsing {} : {} .", response, e);
		}
		return jsonResp;

	}
}
