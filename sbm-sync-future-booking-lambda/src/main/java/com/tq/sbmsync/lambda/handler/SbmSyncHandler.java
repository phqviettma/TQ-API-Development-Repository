package com.tq.sbmsync.lambda.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.common.lambda.exception.TrueQuitBadRequest;
import com.tq.common.lambda.response.LambdaStatusResponse;
import com.tq.sbmsync.lambda.model.SbmSyncReq;

public class SbmSyncHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static int STATUS_CODE = 200;
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private static final Logger m_log = LoggerFactory.getLogger(SbmSyncHandler.class);
	private static final String CLINIKO = "cliniko";
	private static final String GOOGLE = "google";
	private SbmInternalHandler sbmSyncGCHandler = null;
	private SbmInternalHandler sbmSyncClinikoHandler = null;

	public SbmSyncHandler() {
		sbmSyncClinikoHandler = new SbmSyncClinikoHandler();
		sbmSyncGCHandler = new SbmSyncGCHandler();
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
