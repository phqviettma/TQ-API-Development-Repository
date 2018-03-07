package com.tq.googlecalendar.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleConnectStatusResponse;
import com.tq.googlecalendar.lambda.model.GoogleRegisterParams;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;

public class GoogleCalendarCheckStatusHandlerTest {
	private GoogleCalendarDbService calendarService = mock(GoogleCalendarDbService.class);
	private GoogleCalendarCheckStatusHandler checkHandler = new GoogleCalendarCheckStatusHandler(calendarService);

	@Test
	public void testCheckingStatusDisconnect()
			throws TrueQuitRegisterException, GoogleApiSDKException, SbmSDKException, InfSDKExecption {
		GoogleCalendarSbmSync googleCalendarSbmSync = new GoogleCalendarSbmSync("1-7", "phamthanhcute11@gmail.com",
				"phamthanhcute11@gmail.com", "suong", "pham", "", "1/A9smC2Y-21FBLOoU-SOmkWcVuk4ypiGqP7URnrjFjMk",
				"-BLANK-", "x3ZhVWszU5vYU6wJJlg4RaJPKvc");

		when(calendarService.query(any())).thenReturn(googleCalendarSbmSync);
		GoogleRegisterReq req = new GoogleRegisterReq();
		req.setAction("check");
		GoogleRegisterParams params = new GoogleRegisterParams();
		params.setEmail("suongpham53@gmail.com");
		req.setParams(params);
		GoogleConnectStatusResponse resp = checkHandler.handle(req);
		assertEquals(resp.getStatus(), "disconnected");
	}

	@Test
	public void testCheckingStatusConnect()
			throws TrueQuitRegisterException, GoogleApiSDKException, SbmSDKException, InfSDKExecption {

		GoogleRegisterReq req = new GoogleRegisterReq();
		req.setAction("check");
		GoogleRegisterParams params = new GoogleRegisterParams();
		params.setEmail("suongpham53@gmail.com");
		req.setParams(params);
		GoogleConnectStatusResponse resp = checkHandler.handle(req);
		assertEquals(resp.getStatus(), "connected");
	}
}
