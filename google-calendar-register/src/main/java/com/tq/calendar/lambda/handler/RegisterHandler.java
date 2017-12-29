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
import com.tq.common.lambda.dynamodb.dao.CalendarSbmDaoImpl;
import com.tq.common.lambda.dynamodb.impl.CalendarSbmServiceImpl;
import com.tq.common.lambda.dynamodb.model.CalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.CalendarSbmService;
import com.tq.common.lambda.utils.DynamodbUtils;

import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.impl.UnitServiceSbmImpl;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.service.TokenServiceSbm;
import com.tq.simplybook.service.UnitServiceSbm;

public class RegisterHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static final Logger m_log = LoggerFactory.getLogger(RegisterHandler.class);
	private ObjectMapper m_jsonMapper = new ObjectMapper();
	private static int STATUS_CODE = 200;
	private UnitServiceSbm m_unitService = null;
	private TokenServiceSbm m_tokenService = null;
	private Env m_env = null;
	private AmazonDynamoDB m_amazonDynamoDB = null;
	private CalendarSbmService m_calendarService = null;

	public RegisterHandler() {

		this.m_unitService = new UnitServiceSbmImpl();
		this.m_tokenService = new TokenServiceImpl();
		this.m_env = Env.load();
		this.m_amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(m_env.getRegions(), m_env.getAwsAccessKeyId(),
				m_env.getAwsSecretAccessKey());
		;
		this.m_calendarService = new CalendarSbmServiceImpl(new CalendarSbmDaoImpl(m_amazonDynamoDB));
	}

	// for testing only
	RegisterHandler(Env env, AmazonDynamoDB db, UnitServiceSbm unitService, TokenServiceSbm tokenService,
			CalendarSbmService calendarService) {
		this.m_amazonDynamoDB = db;
		this.m_unitService = unitService;
		this.m_tokenService = tokenService;
		this.m_calendarService = calendarService;
		this.m_env = env;
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		m_log.info("Received one request with body " + input.getBody());
		UserInfoResp info = getUserInfo(input.getBody());
		Throwable error = null;
		String companyLogin = m_env.getSimplyBookCompanyLogin();
		String user = m_env.getSimplyBookUser();
		String password = m_env.getSimplyBookPassword();
		String loginEndPoint = m_env.getSimplyBookServiceUrlLogin();
		String endpoint = m_env.getSimplyBookAdminServiceUrl();
		String email = info.getEmail();
		CalendarSbmSync calendarSbm = new CalendarSbmSync();
		if (info != null) {
			try {
				String token = m_tokenService.getUserToken(companyLogin, user, password, loginEndPoint);
				List<UnitProviderInfo> serviceInfo = m_unitService.getUnitList(companyLogin, endpoint, token, true,
						true, 1);
				for (UnitProviderInfo unitInfo : serviceInfo) {
					if (unitInfo.getEmail() != null && unitInfo.getEmail().equals(email)) {
						Set<String> providerInfo = unitInfo.getEvent_map().keySet();
						for (Iterator<String> provider = providerInfo.iterator(); provider.hasNext();) {
							String providerId = provider.next();
							String unitId = unitInfo.getId();
							String sbmId = providerId + "," + unitId;
							calendarSbm.setEmail(email);
							calendarSbm.setSbmId(sbmId);
							calendarSbm.setFirstname(info.getFirstName());
							calendarSbm.setLastname(info.getLastName());
							m_log.info("CalendarSbm Value" + calendarSbm.toString());
							m_calendarService.put(calendarSbm);
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
			info = m_jsonMapper.readValue(value, UserInfoResp.class);
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
		}

		resp.setBody(body);
		resp.setHeaders(input.getHeaders());
		resp.setStatusCode(STATUS_CODE);
	}

}
