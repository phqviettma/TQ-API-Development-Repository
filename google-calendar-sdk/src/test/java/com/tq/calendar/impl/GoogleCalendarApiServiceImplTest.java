package com.tq.calendar.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.tq.calendar.exception.GoogleApiSDKException;
import com.tq.calendar.req.Attendees;
import com.tq.calendar.req.EventReq;
import com.tq.calendar.req.TokenReq;
import com.tq.calendar.resp.End;
import com.tq.calendar.resp.EventResp;
import com.tq.calendar.resp.GoogleCalendarSettingsInfo;
import com.tq.calendar.resp.Start;
import com.tq.calendar.resp.TokenResp;
import com.tq.calendar.service.TokenGoogleCalendarService;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.simplybook.context.Env;

public class GoogleCalendarApiServiceImplTest {
	private Env env = MockUtil.mockEnv();

	private GoogleCalendarApiServiceImpl googleCalendarService = new GoogleCalendarApiServiceImpl("");
	private TokenGoogleCalendarService tokenCalendarService = new TokenGoogleCalendarImpl();

	@Test
	public void testCreateEvent() throws GoogleApiSDKException {
		Start startTime = new Start("2018-01-03T08:13:51.714Z", "Asia/Saigon");
		End endTime = new End("2018-01-03T09:13:51.714Z", "Asia/Saigon");
		String description = "nn";
		List<Attendees> attendees = new ArrayList<>();
		attendees.add(new Attendees("suong", "suong"));
		EventReq req = new EventReq(startTime, endTime, description, attendees, description);
		EventResp events = googleCalendarService.createEvent(req);
		System.out.println(events);
		assertNotNull(events.getId());
	}

	// @Test
	public void testDeleteEvent() throws GoogleApiSDKException {
		Start startTime = new Start("2018-01-03T08:13:51.714Z", "Asia/Saigon");
		End endTime = new End("2018-01-03T09:13:51.714Z", "Asia/Saigon");
		String description = "nn";
		EventReq req = new EventReq(startTime, endTime, description, null, description);
		EventResp events = googleCalendarService.createEvent(req);

		boolean isDeleted = googleCalendarService.deleteEvent(events.getId());
		assertTrue(isDeleted);

	}

	// @Test
	public void testGetEvent() throws GoogleApiSDKException {
		Start startTime = new Start("2018-01-03T08:13:51.714Z", "Asia/Saigon");
		End endTime = new End("2018-01-03T09:13:51.714Z", "Asia/Saigon");
		String description = "nn";
		EventReq req = new EventReq(startTime, endTime, description, null, description);
		EventResp events = googleCalendarService.createEvent(req);

		events = googleCalendarService.getEvent(events.getId());
		assertNotNull(events);
	}

	@Test
	public void testGetToken() throws GoogleApiSDKException {
		String clientId = env.getGoogleClientId();
		String clientSecret = env.getGoogleClientSecrets();
		GoogleCalendarSbmSync calendarSbmSync = new GoogleCalendarSbmSync("1,1", "suongpham53@gmail.com", "suong",
				"pham", "", "", clientSecret, clientSecret, clientSecret);
		TokenReq req = new TokenReq(clientId, clientSecret, calendarSbmSync.getRefreshToken());
		TokenResp resp = tokenCalendarService.getToken(req);
		System.out.println(resp);
	}

	@Test
	public void testGetSetting() throws GoogleApiSDKException {
		GoogleCalendarSettingsInfo settingInfo = googleCalendarService.getSettingInfo("timezone");
		System.out.println(settingInfo);
	}

}
