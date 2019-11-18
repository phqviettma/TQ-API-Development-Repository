package com.tq.simplybook.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.context.Messages;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class SpecialDayHandlerTest {

	private SpecialDayHandler m_handler;
	private TokenServiceSbm m_tokenService;
	private SpecialdayServiceSbm m_specialDayService;
	private Env m_env;
	
	
	@Before
	public void setUp() {
		m_handler = new SpecialDayHandler(){
			@Override
			protected void init() {
				m_tokenService = mock(TokenServiceSbm.class);
				m_specialDayService = mock(SpecialdayServiceSbm.class);
				m_env = mock(Env.class);
				setEnv(m_env);
				setSpecialDayService(m_specialDayService);
				setTokenService(m_tokenService);
				
				when(m_specialDayService.deleteSpecialDay(any(), any(), any(), any())).thenReturn(true);
			}
		};
	}
	
	@Test
	public void testSuccessRequest() {
		AwsProxyRequest request = buildValidParams();
		m_handler.handleRequest(request, null);
	}
	
	@Test
	public void testMissingParameters() {
		AwsProxyRequest request = buildMissingParams();
		AwsProxyResponse response = m_handler.handleRequest(request, null);
		assertEquals(400, response.getStatusCode());
		assertEquals(Messages.MISSING_INPUT_PARAMETERS, response.getBody());
	}
	
	@Test
	public void testInvalidParameters() {
		AwsProxyRequest request = buildInvalidParams();
		AwsProxyResponse response = m_handler.handleRequest(request, null);
		assertEquals(500, response.getStatusCode());
		assertEquals(Messages.FAILED_TO_CLEAR_SPECIAL_DAY + "Invalid input parameters", response.getBody());
	}
	
	@Test
	public void testInvalidDate() {
		AwsProxyRequest request = buildInvalidDateParams();
		AwsProxyResponse response = m_handler.handleRequest(request, null);
		assertEquals(500, response.getStatusCode());
		assertEquals(Messages.FAILED_TO_CLEAR_SPECIAL_DAY + "Invalid startDate or endDate parameters", response.getBody());
	}
	
	private AwsProxyRequest buildValidParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(SpecialDayHandler.START_DATE_PARAM, "20-Nov-2019");
		params.put(SpecialDayHandler.END_DATE_PARAM, "22-Nov-2019");
		params.put(SpecialDayHandler.PROVIDER_ID_PARAM, "30");
		
		AwsProxyRequest request = mock(AwsProxyRequest.class);
		when(request.getQueryStringParameters()).thenReturn(params);
		return request;
	}
	
	private AwsProxyRequest buildMissingParams() {
		AwsProxyRequest request = mock(AwsProxyRequest.class);
		when(request.getQueryStringParameters()).thenReturn(new HashMap<String, String>());
		return request;
	}
	
	private AwsProxyRequest buildInvalidParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("invalidParams", "value");
		AwsProxyRequest request = mock(AwsProxyRequest.class);
		when(request.getQueryStringParameters()).thenReturn(params);
		return request;
	}
	
	private AwsProxyRequest buildInvalidDateParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(SpecialDayHandler.START_DATE_PARAM, "Invalid date");
		params.put(SpecialDayHandler.END_DATE_PARAM, "Invalid date");
		params.put(SpecialDayHandler.PROVIDER_ID_PARAM, "30");
		
		AwsProxyRequest request = mock(AwsProxyRequest.class);
		when(request.getQueryStringParameters()).thenReturn(params);
		return request;
	}
}
