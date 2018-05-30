package com.tq.googlecalendar.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.req.Attendees;
import com.tq.googlecalendar.req.EventReq;
import com.tq.googlecalendar.req.Params;
import com.tq.googlecalendar.req.StopWatchEventReq;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.req.WatchEventReq;
import com.tq.googlecalendar.resp.End;
import com.tq.googlecalendar.resp.ErrorResp;
import com.tq.googlecalendar.resp.EventResp;
import com.tq.googlecalendar.resp.GoogleCalendarList;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.resp.Start;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.resp.WatchEventResp;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;

public class GoogleCalendarApiServiceImplTest {

	private GoogleCalendarApiServiceImpl googleCalendarService = mock(GoogleCalendarApiServiceImpl.class);
	private TokenGoogleCalendarService tokenCalendarService = mock(TokenGoogleCalendarService.class);
	private String googleCalendarId = "primary";

	@Before
	public void init() throws GoogleApiSDKException {
		EventResp eventResp = new EventResp();
		eventResp.setId("111111");
		when(googleCalendarService.createEvent(any(), any())).thenReturn(eventResp);
		when(googleCalendarService.deleteEvent(any(), any())).thenReturn(true);
		when(googleCalendarService.getEvent(any(), any())).thenReturn(eventResp);
		GoogleCalendarSettingsInfo settingInfo = new GoogleCalendarSettingsInfo();
		settingInfo.setId("setting");
		when(googleCalendarService.getSettingInfo(any())).thenReturn(settingInfo);
		WatchEventResp watchResp = new WatchEventResp();
		watchResp.setId("channelId");
		when(googleCalendarService.watchEvent(any(), any())).thenReturn(watchResp);
		ErrorResp errorResp = new ErrorResp();
		when(googleCalendarService.stopWatchEvent(any())).thenReturn(errorResp);
		TokenResp tokenResp = new TokenResp();
		tokenResp.setId_token("tokenId");
		when(tokenCalendarService.getToken(any())).thenReturn(tokenResp);
		GoogleCalendarList resp = new GoogleCalendarList();
		resp.setKind("kind");
		when(googleCalendarService.getListCalendar()).thenReturn(resp );
	}

	@Test
	public void testCreateEvent() throws GoogleApiSDKException {
		Start startTime = new Start("2018-01-03T08:13:51.714Z", "Asia/Saigon");
		End endTime = new End("2018-01-03T09:13:51.714Z", "Asia/Saigon");
		String description = "nn";
		List<Attendees> attendees = new ArrayList<>();
		attendees.add(new Attendees("suong", "suong"));
		EventReq req = new EventReq(startTime, endTime, description, attendees, description);
		EventResp events = googleCalendarService.createEvent(req, googleCalendarId);
		assertNotNull(events.getId());
	}

	@Test
	public void testDeleteEvent() throws GoogleApiSDKException {
		Start startTime = new Start("2018-01-03T08:13:51.714Z", "Asia/Saigon");
		End endTime = new End("2018-01-03T09:13:51.714Z", "Asia/Saigon");
		String description = "nn";
		EventReq req = new EventReq(startTime, endTime, description, null, description);
		EventResp events = googleCalendarService.createEvent(req, googleCalendarId);

		boolean isDeleted = googleCalendarService.deleteEvent(events.getId(), googleCalendarId);
		assertTrue(isDeleted);

	}

	@Test
	public void testGetEvent() throws GoogleApiSDKException {
		Start startTime = new Start("2018-01-03T08:13:51.714Z", "Asia/Saigon");
		End endTime = new End("2018-01-03T09:13:51.714Z", "Asia/Saigon");
		String description = "This is description";
		EventReq req = new EventReq(startTime, endTime, description, null, description);
		EventResp events = googleCalendarService.createEvent(req, googleCalendarId);

		events = googleCalendarService.getEvent(events.getId(), googleCalendarId);
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
		StopWatchEventReq stopEventReq = new StopWatchEventReq("2-4", "9C0dOEpGs7L-ZBJy2BIC6AAQ8ak");
		ErrorResp errorResp = googleCalendarService.stopWatchEvent(stopEventReq);
		assertNull(errorResp.getError());
	}

	@Test
	public void testWatchEvent() throws GoogleApiSDKException {
		Params params = new Params("3600000");
		WatchEventReq eventReq = new WatchEventReq("2-6", "web_hook", "https://clinic.truequit.com/notifications/",
				params);
		WatchEventResp resp = googleCalendarService.watchEvent(eventReq, "example@gmail.com");
		assertNotNull(resp.getId());
	}

	@Test
	public void testGetListCalendar() throws GoogleApiSDKException {
		GoogleCalendarList resp = googleCalendarService.getListCalendar();
		assertNotNull(resp.getKind());
	}
}
