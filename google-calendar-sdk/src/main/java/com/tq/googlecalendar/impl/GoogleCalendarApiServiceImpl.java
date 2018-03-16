package com.tq.googlecalendar.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.req.DeleteGoogleCalendarEventReq;
import com.tq.googlecalendar.req.EventReq;
import com.tq.googlecalendar.req.GetGoogleCalendarApiReq;
import com.tq.googlecalendar.req.GoogleCalendarParser;
import com.tq.googlecalendar.req.PostGoogleCalendarApiReq;
import com.tq.googlecalendar.req.StopWatchEventReq;
import com.tq.googlecalendar.req.UtilsExecutor;
import com.tq.googlecalendar.req.WatchEventReq;
import com.tq.googlecalendar.resp.ApiResponse;
import com.tq.googlecalendar.resp.CalendarEvents;
import com.tq.googlecalendar.resp.ErrorResp;
import com.tq.googlecalendar.resp.EventResp;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.resp.WatchEventResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;

public class GoogleCalendarApiServiceImpl implements GoogleCalendarApiService {
	private final String accessToken;
	private static final Logger m_log = LoggerFactory.getLogger(GoogleCalendarApiServiceImpl.class);

	public GoogleCalendarApiServiceImpl(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public EventResp createEvent(EventReq events) throws GoogleApiSDKException {
		String jsonResp;
		try {
			ApiResponse response = UtilsExecutor.request(new CreateEvent(accessToken, events));
			jsonResp = response.getEntity();
			m_log.info("createEvent json response: " + String.valueOf(jsonResp));
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, EventResp.class);

		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
	}

	@Override
	public boolean deleteEvent(String eventId) throws GoogleApiSDKException {

		try {
			UtilsExecutor.request(new DeleteEvent(accessToken, eventId));
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
		return true;

	}

	@Override
	public ErrorResp stopWatchEvent(StopWatchEventReq stopEventReq) throws GoogleApiSDKException {
		String jsonResp;
		try {

			ApiResponse response = UtilsExecutor.request(new StopWatchEvent(accessToken, stopEventReq));
			jsonResp = response.getEntity();
			Integer statusCode = response.getStatusCode();
			if (statusCode == 204) {
				return null;
			}
			else {
				return GoogleCalendarParser.readJsonValueForObject(jsonResp, ErrorResp.class);
			}
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
	}

	private class GetEventNextPage extends GetGoogleCalendarApiReq {

		public GetEventNextPage(String accessToken, Integer maxResults, String syncToken, String nextPageToken) {
			super(accessToken, "calendars/primary/events?maxResults=" + maxResults
					+ "&singleEvents=true&showDeleted=true&syncToken=" + syncToken + "&pageToken=" + nextPageToken);

		}

	}

	private class CreateEvent extends PostGoogleCalendarApiReq {

		public CreateEvent(String accessToken, Object object) {
			super(accessToken, "calendars/primary/events", object);

		}

	}

	private class DeleteEvent extends DeleteGoogleCalendarEventReq {

		public DeleteEvent(String accessToken, String eventId) {
			super(accessToken, "calendars/primary/events/" + eventId + "?" + "sendNotifications=false");

		}

	}

	private class StopWatchEvent extends PostGoogleCalendarApiReq {

		public StopWatchEvent(String accessToken, Object object) {
			super(accessToken, "channels/stop", object);

		}

	}

	@Override
	public EventResp getEvent(String eventId) throws GoogleApiSDKException {
		String jsonResp;
		try {
			ApiResponse response = UtilsExecutor.request(new GetEvent(accessToken, eventId));
			jsonResp = response.getEntity();
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, EventResp.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
	}

	private class GetEvent extends GetGoogleCalendarApiReq {

		public GetEvent(String accessToken, String eventId) {
			super(accessToken, "calendars/primary/events/" + eventId);

		}

	}

	@Override
	public WatchEventResp watchEvent(WatchEventReq req, String email) throws GoogleApiSDKException {
		String jsonResp;
		try {
			ApiResponse response = UtilsExecutor.request(new WatchEvent(accessToken, email, req));
			jsonResp = response.getEntity();
			m_log.info("Watch event json response" + String.valueOf(jsonResp));
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, WatchEventResp.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}

	}

	private class WatchEvent extends PostGoogleCalendarApiReq {

		public WatchEvent(String accessToken, String email, Object object) {
			super(accessToken, "calendars/" + email + "/events/watch", object);

		}

	}

	@Override
	public CalendarEvents getEventWithNextPageToken(Integer maxResult, String syncToken, String pageToken)
			throws GoogleApiSDKException {
		String jsonResp;
		try {

			ApiResponse response = UtilsExecutor
					.request(new GetEventNextPage(accessToken, maxResult, syncToken, pageToken));
			jsonResp = response.getEntity();
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, CalendarEvents.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}

	}

	@Override
	public CalendarEvents getEventWithoutToken(Integer maxResult, String timeMin) throws GoogleApiSDKException {
		String jsonResp;
		try {
			ApiResponse response = UtilsExecutor.request(new GetEventWithoutToken(accessToken, maxResult, timeMin));
			jsonResp = response.getEntity();
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, CalendarEvents.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}

	}

	private class GetEventWithoutToken extends GetGoogleCalendarApiReq {

		public GetEventWithoutToken(String accessToken, Integer maxResult, String timeMin) throws Exception {
			super(accessToken, "calendars/primary/events?maxResults=" + maxResult + "&singleEvents=true&timeMin="
					+ URLEncoder.encode(timeMin, "UTF-8"));

		}

	}

	@Override
	public GoogleCalendarSettingsInfo getSettingInfo(String settingId) throws GoogleApiSDKException {
		String jsonResp;
		try {
			ApiResponse response = UtilsExecutor.request(new GetSettingInfo(accessToken, settingId));
			jsonResp = response.getEntity();
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, GoogleCalendarSettingsInfo.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}

	}

	private class GetSettingInfo extends GetGoogleCalendarApiReq {

		public GetSettingInfo(String accessToken, String settingId) {
			super(accessToken, "users/me/settings/" + settingId);

		}

	}

	@Override
	public CalendarEvents getEventWithNextSyncToken(Integer maxResult, String nextSyncToken)
			throws GoogleApiSDKException {
		String jsonResp;
		try {
			ApiResponse response = UtilsExecutor
					.request(new GetEventWithNextSyncToken(accessToken, maxResult, nextSyncToken));
			jsonResp = response.getEntity();
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, CalendarEvents.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
	}

	private class GetEventWithNextSyncToken extends GetGoogleCalendarApiReq {

		public GetEventWithNextSyncToken(String accessToken, Integer maxResult, String syncToken) {
			super(accessToken, "calendars/primary/events?maxResults=" + maxResult
					+ "&singleEvents=true&showDeleted=true&syncToken=" + syncToken);

		}

	}

	@Override
	public CalendarEvents getEventAtLastTime(Integer maxResult, String lastQueryTimeMin, String nextPageToken)
			throws GoogleApiSDKException {
		String jsonResp;
		try {

			ApiResponse response = UtilsExecutor
					.request(new GetEventAtTime(accessToken, maxResult, nextPageToken, lastQueryTimeMin));
			jsonResp = response.getEntity();
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, CalendarEvents.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
	}

	private class GetEventAtTime extends GetGoogleCalendarApiReq {

		public GetEventAtTime(String accessToken, Integer maxResult, String nextPageToken, String lastQueryTimeMin)
				throws UnsupportedEncodingException {
			super(accessToken,
					"calendars/primary/events?maxResults=" + maxResult
							+ "&singleEvents=true&showDeleted=true&pageToken=" + nextPageToken + "&timeMin="
							+ URLEncoder.encode(lastQueryTimeMin, "UTF-8"));

		}

	}

}
