package com.tq.truequit.web.lambda.handler;

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
import com.tq.common.lambda.dynamodb.dao.SbmBookingInfoDaoImpl;
import com.tq.common.lambda.dynamodb.impl.ContactItemServiceImpl;
import com.tq.common.lambda.dynamodb.impl.SbmBookingInfoServiceImpl;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.SbmBookingInfoService;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;
import com.tq.truequit.lambda.context.ShowBookingLambdaEnv;
import com.tq.truequit.web.lambda.response.ShowBookingInfoResponse;

public class TQBookingHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static int STATUS_CODE = 200;
	private static final String SHOW = "show";
	private static final String CANCEL = "cancel";
	private static final String NO_SHOW = "no_show";
	private static final String ARRIVED = "arrived";
	private static final String SHOW_MEMBER_BOOKING = "member_booking";
	private static final Logger m_log = LoggerFactory.getLogger(TQBookingHandler.class);
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private BookingServiceSbm bookingService = null;
	private SbmBookingInfoService sbmBookingInfoService = null;
	private ShowBookingLambdaEnv env = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private ContactServiceInf contactInfService = null;
	private ContactItemService contactService = null;
	private BookingHandler showBookingHandler = null;
	private BookingHandler cancelBookingHandler = null;
	private BookingHandler confirmArriveHandler = null;
	private BookingHandler confirmNoShowHandler = null;
	private BookingHandler showMemberBooking = null;

	public TQBookingHandler() {
		this.env = ShowBookingLambdaEnv.load();
		;
		this.bookingService = new BookingServiceSbmImpl();
		this.tokenServiceSbm = new TokenServiceImpl();
		this.contactInfService = new ContactServiceImpl();
		AmazonDynamoDB client = DynamodbUtils.getAmazonDynamoDB(env.getRegions(), env.getAwsAccessKeyId(),
				env.getAwsSecretAccessKey());
		this.sbmBookingInfoService = new SbmBookingInfoServiceImpl(new SbmBookingInfoDaoImpl(client));
		this.contactService = new ContactItemServiceImpl(new ContactItemDaoImpl(client));
		this.showBookingHandler = new ShowBookingInfoHandler(sbmBookingInfoService);
		this.cancelBookingHandler = new TQCancelBookingHandler(bookingService, env, tokenServiceSbm, contactInfService,
				contactService, sbmBookingInfoService);
		this.confirmArriveHandler = new TQConfirmArriveBookingHandler(bookingService, env, tokenServiceSbm,
				contactInfService, contactService, sbmBookingInfoService);
		this.confirmNoShowHandler = new TQConfirmNoShowHandler(bookingService, env, tokenServiceSbm, contactInfService,
				contactService, sbmBookingInfoService);
		this.showMemberBooking = new TQShowMemberBooking(sbmBookingInfoService);
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Access-Control-Allow-Origin", "*");
		headers.put("content-Type", "application/json");
		resp.setHeaders(headers);
		ShowBookingInfoResponse response = new ShowBookingInfoResponse();
		ShowBookingRequest request = getRequest(input.getBody());
		try {
			switch (request.getAction()) {
			case SHOW:

				response = showBookingHandler.handle(request);

				break;
			case CANCEL:
				response = cancelBookingHandler.handle(request);
				break;
			case NO_SHOW:
				response = confirmNoShowHandler.handle(request);
				break;
			case ARRIVED:
				response = confirmArriveHandler.handle(request);
				break;
			case SHOW_MEMBER_BOOKING:
				response = showMemberBooking.handle(request);
				break;
			default:
				break;
			}
		} catch (SbmSDKException | InfSDKExecption e) {
			resp.setStatusCode(500);
		}
		String jsonResp = buildResponse(response);
		resp.setBody(jsonResp);
		resp.setStatusCode(STATUS_CODE);
		return resp;
	}

	private ShowBookingRequest getRequest(String body) {
		ShowBookingRequest req = null;
		try {
			req = jsonMapper.readValue(body, ShowBookingRequest.class);
		} catch (IOException e) {
			m_log.error("Error during parsing {} : {} .", body, e);
		}
		return req;
	}

	private static String buildResponse(ShowBookingInfoResponse response) {
		String jsonResp = null;
		try {
			jsonResp = jsonMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			m_log.info("Error during parsing {} : {} .", response, e);
		}
		return jsonResp;

	}
}
