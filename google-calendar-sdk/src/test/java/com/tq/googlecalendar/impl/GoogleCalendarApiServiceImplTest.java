package com.tq.googlecalendar.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.req.Attendees;
import com.tq.googlecalendar.req.EventReq;
import com.tq.googlecalendar.req.StopWatchEventReq;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.req.WatchEventReq;
import com.tq.googlecalendar.resp.End;
import com.tq.googlecalendar.resp.EventResp;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.resp.Start;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.resp.WatchEventResp;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;

public class GoogleCalendarApiServiceImplTest {
	
	private GoogleCalendarApiServiceImpl googleCalendarService = new GoogleCalendarApiServiceImpl(
			"");
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

	@Test
	public void testDeleteEvent() throws GoogleApiSDKException {
		Start startTime = new Start("2018-01-03T08:13:51.714Z", "Asia/Saigon");
		End endTime = new End("2018-01-03T09:13:51.714Z", "Asia/Saigon");
		String description = "nn";
		EventReq req = new EventReq(startTime, endTime, description, null, description);
		EventResp events = googleCalendarService.createEvent(req);

		boolean isDeleted = googleCalendarService.deleteEvent(events.getId());
		assertTrue(isDeleted);

	}

	@Test
	public void testGetEvent() throws GoogleApiSDKException {
		Start startTime = new Start("2018-01-03T08:13:51.714Z", "Asia/Saigon");
		End endTime = new End("2018-01-03T09:13:51.714Z", "Asia/Saigon");
		String description = "This is description";
		EventReq req = new EventReq(startTime, endTime, description, null, description);
		EventResp events = googleCalendarService.createEvent(req);

		events = googleCalendarService.getEvent(events.getId());
		assertNotNull(events);
	}

	@Test
	public void testGetToken() throws GoogleApiSDKException {
		String clientId = "";
		String clientSecret = "";
		String refreshToken = "";
		TokenReq req = new TokenReq(clientId, clientSecret, refreshToken);
		TokenResp resp = tokenCalendarService.getToken(req);
		assertNotNull(resp.getId_token());
	}

	@Test
	public void testGetSetting() throws GoogleApiSDKException {
		GoogleCalendarSettingsInfo settingInfo = googleCalendarService.getSettingInfo("timezone");
		assertNotNull(settingInfo.getId());
	}

	@Test
	public void testStopWatchEvent() throws GoogleApiSDKException {
		StopWatchEventReq stopEventReq = new StopWatchEventReq("1-6", "x3ZhVWszU5vYU6wJJlg4RaJPKvc");
		boolean isStopped = googleCalendarService.stopWatchEvent(stopEventReq);
		assertTrue(isStopped);
	}

	@Test
	public void testWatchEvent() throws GoogleApiSDKException {
		//Params params = new Params("3600000");
		WatchEventReq eventReq = new WatchEventReq("2-6", "web_hook", "https://clinic.truequit.com/notifications/");
		WatchEventResp resp = googleCalendarService.watchEvent(eventReq, "ambrose.gregory21@gmail.com");
		assertNotNull(resp);
	}
}
