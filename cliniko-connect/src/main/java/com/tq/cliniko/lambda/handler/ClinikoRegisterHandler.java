package com.tq.cliniko.lambda.handler;

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
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.context.ClinikoEnv;
import com.tq.cliniko.lambda.exception.ClinikoExeptionHandler;
import com.tq.cliniko.lambda.model.ClinikoPractitionerConnectReq;
import com.tq.cliniko.lambda.resp.ClinikoConnectFailureResponse;
import com.tq.cliniko.lambda.resp.ClinikoConnectStatusResponse;
import com.tq.common.lambda.dynamodb.dao.ClinikoCompanyInfoDaoImpl;
import com.tq.common.lambda.dynamodb.dao.ClinikoItemDaoImpl;
import com.tq.common.lambda.dynamodb.dao.ClinikoSyncToSbmDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmListBookingDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmSyncFutureBookingDaoImpl;
import com.tq.common.lambda.dynamodb.impl.ClinikoCompanyInfoServiceImpl;
import com.tq.common.lambda.dynamodb.impl.ClinikoItemServiceImpl;
import com.tq.common.lambda.dynamodb.impl.ClinikoSyncToSbmServiceImpl;
import com.tq.common.lambda.dynamodb.impl.SbmListBookingServiceImpl;
import com.tq.common.lambda.dynamodb.impl.SbmSyncFutureBookingServiceImpl;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoItemService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.SbmUnitServiceImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.TokenServiceSbm;

public class ClinikoRegisterHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static int STATUS_CODE = 200;
	private static final String CONNECT = "connect";
	private static final String DISCONNECT = "disconnect";
	private static final String CHECK = "check";
	private static final Logger m_log = LoggerFactory.getLogger(ClinikoRegisterHandler.class);
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private SbmUnitService unitServiceSbm = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private ClinikoEnv eVariables = null;
	private AmazonDynamoDB amazonDynamoDB = null;
	private ClinikoSyncToSbmService clinikoSyncService = null;
	private ConnectHandler connectHandler = null;
	private ConnectHandler disconnectHandler = null;
	private ConnectHandler checkingHandler = null;
	private ClinikoItemService clinikoItemService = null;
	private ClinikoCompanyInfoService clinikoCompanyService = null;
	private SbmSyncFutureBookingsService sbmSyncFutureBookingService = null;
	private BookingServiceSbmImpl bookingService = null;
	private SbmListBookingService sbmBookingDBService = null;
	private ClinikoApiServiceBuilder clinikoApiServiceBuilder = null;

	public ClinikoRegisterHandler() {
		this.eVariables = ClinikoEnv.load();
		this.tokenServiceSbm = new TokenServiceImpl();
		this.clinikoApiServiceBuilder = new ClinikoApiServiceBuilder();
		this.amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(eVariables.getRegions(), eVariables.getAwsAccessKeyId(),
				eVariables.getAwsSecretAccessKey());
		this.bookingService = new BookingServiceSbmImpl();
		this.sbmBookingDBService = new SbmListBookingServiceImpl(new SbmListBookingDaoImpl(amazonDynamoDB));
		this.unitServiceSbm = new SbmUnitServiceImpl();
		this.clinikoSyncService = new ClinikoSyncToSbmServiceImpl(new ClinikoSyncToSbmDaoImpl(amazonDynamoDB));
		this.clinikoItemService = new ClinikoItemServiceImpl(new ClinikoItemDaoImpl(amazonDynamoDB));
		this.sbmSyncFutureBookingService = new SbmSyncFutureBookingServiceImpl(
				new SbmSyncFutureBookingDaoImpl(amazonDynamoDB));
		this.clinikoCompanyService = new ClinikoCompanyInfoServiceImpl(new ClinikoCompanyInfoDaoImpl(amazonDynamoDB));
		this.disconnectHandler = new ClinikoDisconnectHandler(clinikoSyncService, clinikoItemService,
				sbmSyncFutureBookingService, sbmBookingDBService);
		this.checkingHandler = new CheckingHandler(clinikoSyncService);
		this.connectHandler = new ClinikoConnectHandler(eVariables, unitServiceSbm, tokenServiceSbm, clinikoSyncService,
				clinikoItemService, clinikoCompanyService, sbmSyncFutureBookingService, bookingService,
				sbmBookingDBService, clinikoApiServiceBuilder);

	}

	ClinikoRegisterHandler(ClinikoEnv env, AmazonDynamoDB db, SbmUnitService unitService, TokenServiceSbm tokenService,
			ClinikoSyncToSbmService clinikoDbService, ClinikoConnectHandler connectHandler,
			ClinikoDisconnectHandler disconnectHandler, CheckingHandler checkingHandler) {
		this.amazonDynamoDB = db;
		this.unitServiceSbm = unitService;
		this.tokenServiceSbm = tokenService;
		this.clinikoSyncService = clinikoDbService;
		this.eVariables = env;
		this.connectHandler = connectHandler;
		this.disconnectHandler = disconnectHandler;
		this.checkingHandler = checkingHandler;
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		m_log.info("Received one request with body " + input.getBody());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("content-Type", "application/json");
		resp.setHeaders(headers);
		ClinikoConnectStatusResponse response = new ClinikoConnectStatusResponse();
		ClinikoPractitionerConnectReq req = getPractitionerReq(input.getBody());
		try {
			if (CONNECT.equals(req.getAction())) {
				response = connectHandler.handle(req);
			} else if (DISCONNECT.equals(req.getAction())) {
				response = disconnectHandler.handle(req);
			} else if (CHECK.equals(req.getAction())) {
				response = checkingHandler.handle(req);
			}

		} catch (Exception e) {
			m_log.error("Error occurs", e);
			ClinikoConnectFailureResponse failResponse = ClinikoExeptionHandler.handle(e);
			String jsonResp = ClinikoExeptionHandler.buildFailMessageResponse(failResponse.isSucceeded(),
					failResponse.getErrorMessage());
			resp.setBody(jsonResp);
			resp.setStatusCode(failResponse.getStatusCode());
			return resp;
		}

		String jsonResp = buildResponseBody(response);
		resp.setBody(jsonResp);
		resp.setStatusCode(STATUS_CODE);
		return resp;
	}

	public ClinikoPractitionerConnectReq getPractitionerReq(String value) {
		ClinikoPractitionerConnectReq info = null;
		try {
			info = jsonMapper.readValue(value, ClinikoPractitionerConnectReq.class);
		} catch (Exception e) {
			m_log.error("Error during parsing {} : {} .", value, e);
		}
		return info;

	}

	private static String buildResponseBody(ClinikoConnectStatusResponse response) {
		String jsonResp = null;
		try {
			jsonResp = jsonMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			m_log.info("Error during parsing {} : {} .", response, e);
		}
		return jsonResp;
	}
}
