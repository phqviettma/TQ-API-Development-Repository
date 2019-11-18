package com.tq.simplybook.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tq.common.lambda.utils.TimeUtils;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.context.Messages;
import com.tq.simplybook.impl.SpecialdayServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.req.DeleteSpecialDayReq;
import com.tq.simplybook.resp.ProviderInfo;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class SpecialDayHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

	public static final Logger m_logger = LoggerFactory.getLogger(SpecialDayHandler.class);
	public static final String START_DATE_PARAM = "startDate";
	public static final String END_DATE_PARAM = "endDate";
	public static final String PROVIDER_ID_PARAM = "providerId";
	public static final String DEFAULT_EVENT_ID = "2";
	private TokenServiceSbm m_tokenService;
	private SpecialdayServiceSbm m_specialDayService;
	private Env m_env;
	
	public SpecialDayHandler() {
		init();
	}
	
	protected void init() {
		setTokenService(new TokenServiceImpl());
		setSpecialDayService(new SpecialdayServiceSbmImpl());
		setEnv(Env.load());
	}
	
	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		Map<String, String> queryStringParameters = input.getQueryStringParameters();
		try {
			if (queryStringParameters == null || queryStringParameters.isEmpty()) {
				m_logger.info("Missing input parameters");
				resp.setBody(Messages.MISSING_INPUT_PARAMETERS);
				resp.setStatusCode(400);
				return resp;
			}
			
			m_logger.info("Input parameters = {}", queryStringParameters);
			if (!queryStringParameters.isEmpty()) {
				clearSpecialDay(queryStringParameters);
				m_logger.info("Cleared special day successfully");
				resp.setBody(Messages.CLEAR_SPECIAL_DAY_SUCCESS);
				resp.setStatusCode(200);
			}
		} catch (Exception e) {
			m_logger.error("Failed to clear special day: ", e);
			resp.setBody(Messages.FAILED_TO_CLEAR_SPECIAL_DAY + e.getMessage());
			resp.setStatusCode(500);
		}
		return resp;
		
	}
	
	private void clearSpecialDay(Map<String, String> queryStringParameters) throws Exception {
		String inputStartDate = queryStringParameters.get(START_DATE_PARAM);
		String inputEndDate = queryStringParameters.get(END_DATE_PARAM);
		String inputProviderId = queryStringParameters.get(PROVIDER_ID_PARAM);
		
		if (inputStartDate == null || inputEndDate == null || inputProviderId == null) {
			throw new Exception("Invalid input parameters");
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = formatter.parse(inputStartDate);
			endDate = formatter.parse(inputEndDate);
		} catch (Exception e) {
			throw new Exception("Invalid startDate or endDate parameters");
		}
		
		Calendar startDateCalendar = Calendar.getInstance();
		Calendar endDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(startDate);
		endDateCalendar.setTime(endDate);
		
		List<DeleteSpecialDayReq> requests = new ArrayList<DeleteSpecialDayReq>();
		while (!startDateCalendar.after(endDateCalendar)){
			String date = TimeUtils.formatDateSbm(startDateCalendar.getTime());
			requests.add(buildDeleteSpecialDayRequest(date, inputProviderId));
			startDateCalendar.add(Calendar.DATE, 1);
		}
		
		String companyLogin = getEnv().getSimplyBookCompanyLogin();
		String endpointLogin = getEnv().getSimplyBookServiceUrlLogin();
		String endpoint = getEnv().getSimplyBookAdminServiceUrl();
		String username = getEnv().getSimplyBookUser();
		String password = getEnv().getSimplyBookPassword();
		String token = null;
		try {
			token = getTokenService().getUserToken(companyLogin, username, password, endpointLogin);
		} catch (Exception e) {
			throw new Exception("Could not get user token to interact with SBM");
		}
		
		for(DeleteSpecialDayReq req : requests) {
			boolean result = getSpecialDayService().deleteSpecialDay(companyLogin, endpoint, token, req);
			m_logger.info("date = {}, result = {}", req.getRequest().keySet(), result);
		}
	}
	
	private DeleteSpecialDayReq buildDeleteSpecialDayRequest(String date, String providerId) {
		ProviderInfo info = new ProviderInfo();
		info.setProviderId(providerId);
		info.setEventId(DEFAULT_EVENT_ID);
		Map<String, ProviderInfo> detail = new HashMap<String, ProviderInfo>();
		detail.put(date, info);
		DeleteSpecialDayReq request = new DeleteSpecialDayReq();
		request.setRequest(detail);
		return request;
	}

	protected void setTokenService(TokenServiceSbm tokenService) {
		this.m_tokenService = tokenService;
	}
	
	protected TokenServiceSbm getTokenService() {
		return m_tokenService;
	}

	protected SpecialdayServiceSbm getSpecialDayService() {
		return m_specialDayService;
	}

	protected void setSpecialDayService(SpecialdayServiceSbm m_specialDayService) {
		this.m_specialDayService = m_specialDayService;
	}

	protected Env getEnv() {
		return m_env;
	}

	protected void setEnv(Env m_env) {
		this.m_env = m_env;
	}
}
