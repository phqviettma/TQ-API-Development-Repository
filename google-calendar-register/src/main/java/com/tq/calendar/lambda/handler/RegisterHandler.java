package com.tq.calendar.lambda.handler;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.calendar.lambda.context.Env;
import com.tq.calendar.lambda.model.UserInfoResp;
import com.tq.common.lambda.dynamodb.dao.GoogleCalendarDaoImpl;
import com.tq.common.lambda.dynamodb.impl.GoogleCalendarServiceImpl;
import com.tq.common.lambda.dynamodb.model.CalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarService;
import com.tq.common.lambda.utils.DynamodbUtils;

import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.impl.UnitServiceSbmImpl;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.service.TokenServiceSbm;
import com.tq.simplybook.service.UnitServiceSbm;

public class RegisterHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static final Logger m_log = LoggerFactory.getLogger(RegisterHandler.class);
	private ObjectMapper jsonMapper = new ObjectMapper();
	private static int STATUS_CODE = 200;
	private UnitServiceSbm unitServiceSbm = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private Env eVariables = null;
	private AmazonDynamoDB amazonDynamoDB = null;
	private GoogleCalendarService googleCalendarService = null;

	public RegisterHandler() {

		this.unitServiceSbm = new UnitServiceSbmImpl();
		this.tokenServiceSbm = new TokenServiceImpl();
		this.eVariables = Env.load();
		this.amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(eVariables.getRegions(), eVariables.getAwsAccessKeyId(),
				eVariables.getAwsSecretAccessKey());
		;
		this.googleCalendarService = new GoogleCalendarServiceImpl(new GoogleCalendarDaoImpl(amazonDynamoDB));
	}

	// for testing only
	RegisterHandler(Env env, AmazonDynamoDB db, UnitServiceSbm unitService, TokenServiceSbm tokenService,
			GoogleCalendarService calendarService) {
		this.amazonDynamoDB = db;
		this.unitServiceSbm = unitService;
		this.tokenServiceSbm = tokenService;
		this.googleCalendarService = calendarService;
		this.eVariables = env;
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		m_log.info("Received one request with body " + input.getBody());
		UserInfoResp info = getUserInfo(input.getBody());
		Throwable error = null;
		if (info != null) {
			try {
				String companyLogin = eVariables.getSimplyBookCompanyLogin();
				String user = eVariables.getSimplyBookUser();
				String password = eVariables.getSimplyBookPassword();
				String loginEndPoint = eVariables.getSimplyBookServiceUrlLogin();
				String endpoint = eVariables.getSimplyBookAdminServiceUrl();
				String email = info.getEmail();
				String token = tokenServiceSbm.getUserToken(companyLogin, user, password, loginEndPoint);
				List<UnitProviderInfo> serviceInfo = unitServiceSbm.getUnitList(companyLogin, endpoint, token, true,
						true, 1);
				for (UnitProviderInfo unitInfo : serviceInfo) {
					if (unitInfo.getEmail() != null && unitInfo.getEmail().equals(email)) {
						Set<String> providerInfo = unitInfo.getEvent_map().keySet();
						for (Iterator<String> provider = providerInfo.iterator(); provider.hasNext();) {
							String providerId = provider.next();
							String unitId = unitInfo.getId();
							String sbmId = providerId + "," + unitId;
							CalendarSbmSync calendarSbm = new CalendarSbmSync(sbmId, email, info.getLastName(),
									info.getLastName());
							m_log.info("CalendarSbm Value" + calendarSbm.toString());
							googleCalendarService.put(calendarSbm);
							m_log.info("Added to database successfully" + calendarSbm.toString());
						}

					}
				}
			} catch (Exception e) {
				m_log.error("Error occurs", e);
				error = e;
				resp.setStatusCode(500);
			}
		}
		handleResponse(input, resp, info, error);
		return resp;
	}

	public UserInfoResp getUserInfo(String value) {
		UserInfoResp info = null;
		try {
			info = jsonMapper.readValue(value, UserInfoResp.class);
		} catch (IOException e) {
			m_log.error("Error during parsing {} : {} .", value, e);
		}
		return info;
	}

	private void handleResponse(AwsProxyRequest input, AwsProxyResponse resp, UserInfoResp info, Throwable t) {
		String body = null;
		if (info != null) {
			if (t != null) {
				body = "ERROR";
			} else {
				body = "OK: ";
			}
		} else {
			body = "Invalid request body";
			resp.setStatusCode(400);
		}

		resp.setBody(body);
		resp.setHeaders(input.getHeaders());
		resp.setStatusCode(STATUS_CODE);
	}

}
