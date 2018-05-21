package com.tq.googlecalendar.impl;

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
import com.tq.googlecalendar.resp.GoogleCalendarList;
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
	public EventResp createEvent(EventReq events, String googleCalendarId) throws GoogleApiSDKException {
		String jsonResp;
		try {
			ApiResponse response = UtilsExecutor.request(new CreateEvent(accessToken, events, googleCalendarId));
			jsonResp = response.getEntity();
			m_log.info("createEvent json response: " + String.valueOf(jsonResp));
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, EventResp.class);

		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
	}

	@Override
	public boolean deleteEvent(String eventId, String googleCalendarId) throws GoogleApiSDKException {

		try {
			UtilsExecutor.request(new DeleteEvent(accessToken, eventId, googleCalendarId));
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
			} else {
				return GoogleCalendarParser.readJsonValueForObject(jsonResp, ErrorResp.class);
			}
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
	}

	private class GetEventNextPage extends GetGoogleCalendarApiReq {

		public GetEventNextPage(String accessToken, Integer maxResults, String syncToken, String nextPageToken,
				String googleCalendarId) {
			super(accessToken, "calendars/" + googleCalendarId + "/events?maxResults=" + maxResults
					+ "&singleEvents=true&showDeleted=true&syncToken=" + syncToken + "&pageToken=" + nextPageToken);

		}

	}

	private class CreateEvent extends PostGoogleCalendarApiReq {

		public CreateEvent(String accessToken, Object object, String googleCalendarId) {
			super(accessToken, "calendars/" + googleCalendarId + "/events", object);

		}

	}

	private class DeleteEvent extends DeleteGoogleCalendarEventReq {

		public DeleteEvent(String accessToken, String eventId, String googleCalendarId) {
			super(accessToken,
					"calendars/" + googleCalendarId + "/events/" + eventId + "?" + "sendNotifications=false");

		}

	}

	private class StopWatchEvent extends PostGoogleCalendarApiReq {

		public StopWatchEvent(String accessToken, Object object) {
			super(accessToken, "channels/stop", object);

		}

	}

	@Override
	public EventResp getEvent(String eventId, String googleCalendarId) throws GoogleApiSDKException {
		String jsonResp;
		try {
			ApiResponse response = UtilsExecutor.request(new GetEvent(accessToken, eventId, googleCalendarId));
			jsonResp = response.getEntity();
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, EventResp.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
	}

	private class GetEvent extends GetGoogleCalendarApiReq {

		public GetEvent(String accessToken, String eventId, String googleCalendarId) {
			super(accessToken, "calendars/" + googleCalendarId + "/events/" + eventId);

		}

	}

	@Override
	public WatchEventResp watchEvent(WatchEventReq req, String googleCalendarId) throws GoogleApiSDKException {
		String jsonResp;
		try {
			ApiResponse response = UtilsExecutor.request(new WatchEvent(accessToken, googleCalendarId, req));
			jsonResp = response.getEntity();
			m_log.info("Watch event json response" + String.valueOf(jsonResp));
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, WatchEventResp.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}

	}

	private class WatchEvent extends PostGoogleCalendarApiReq {

		public WatchEvent(String accessToken, String googleCalendarId, Object object) {
			super(accessToken, "calendars/" + googleCalendarId + "/events/watch", object);

		}

	}

	@Override
	public CalendarEvents getEventWithNextPageToken(Integer maxResult, String syncToken, String pageToken,
			String googleCalendarId) throws GoogleApiSDKException {
		String jsonResp;
		try {

			ApiResponse response = UtilsExecutor
					.request(new GetEventNextPage(accessToken, maxResult, syncToken, pageToken, googleCalendarId));
			jsonResp = response.getEntity();
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, CalendarEvents.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}

	}

	@Override
	public CalendarEvents getEventWithoutToken(Integer maxResult, String timeMin, String query, String googleCalendarId)
			throws GoogleApiSDKException {
		String jsonResp;
		try {
			ApiResponse response = UtilsExecutor
					.request(new QueryEventAtSpecificTime(accessToken, maxResult, query, timeMin, googleCalendarId));
			jsonResp = response.getEntity();
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, CalendarEvents.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}

	}

	private class QueryEventAtSpecificTime extends GetGoogleCalendarApiReq {

		public QueryEventAtSpecificTime(String accessToken, Integer maxResult, String query, String queryTime,
				String googleCalendarId) throws Exception {
			super(accessToken, "calendars/" + googleCalendarId + "/events?maxResults=" + maxResult
					+ "&singleEvents=true&" + query + "=" + URLEncoder.encode(queryTime, "UTF-8"));

		}

	}

	private class QueryNewestEvent extends GetGoogleCalendarApiReq {

		public QueryNewestEvent(String accessToken, Integer maxResult, String queryTime, String googleCalendarId)
				throws Exception {
			super(accessToken,
					"calendars/" + googleCalendarId + "/events?maxResults=" + maxResult
							+ "&orderBy=updated&showDeleted=true&singleEvents=true&updatedMin="
							+ URLEncoder.encode(queryTime, "UTF-8"));

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
	public CalendarEvents getEventWithNextSyncToken(Integer maxResult, String nextSyncToken, String googleCalendarId)
			throws GoogleApiSDKException {
		String jsonResp;
		try {
			ApiResponse response = UtilsExecutor
					.request(new GetEventWithNextSyncToken(accessToken, maxResult, nextSyncToken, googleCalendarId));
			jsonResp = response.getEntity();
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, CalendarEvents.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
	}

	private class GetEventWithNextSyncToken extends GetGoogleCalendarApiReq {

		public GetEventWithNextSyncToken(String accessToken, Integer maxResult, String syncToken,
				String googleCalendarId) {
			super(accessToken, "calendars/" + googleCalendarId + "/events?maxResults=" + maxResult
					+ "&singleEvents=true&showDeleted=true&syncToken=" + syncToken);

		}

	}

	@Override
	public CalendarEvents getUpdatedEventWithPageToken(Integer maxResult, String lastQueryTimeMin, String nextPageToken,
			String googleCalendarId) throws GoogleApiSDKException {
		String jsonResp;
		try {

			ApiResponse response = UtilsExecutor.request(new GetEventWithLastTimeQuery(accessToken, maxResult,
					lastQueryTimeMin, nextPageToken, googleCalendarId));
			jsonResp = response.getEntity();
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, CalendarEvents.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
	}

	private class GetEventWithLastTimeQuery extends GetGoogleCalendarApiReq {

		public GetEventWithLastTimeQuery(String accessToken, Integer maxResult, String lastQueryTimeMin,
				String nextPageToken, String googleCalendarId) throws Exception {
			super(accessToken,
					"calendars/" + googleCalendarId + "/events?maxResults=" + maxResult + "&pageToken=" + nextPageToken
							+ "&orderBy=updated&showDeleted=true&singleEvents=true&updatedMin="
							+ URLEncoder.encode(lastQueryTimeMin, "UTF-8"));

		}

	}

	@Override
	public CalendarEvents queryNewestEvent(Integer maxResult, String time, String googleCalendarId)
			throws GoogleApiSDKException {
		String jsonResp;
		try {
			ApiResponse response = UtilsExecutor
					.request(new QueryNewestEvent(accessToken, maxResult, time, googleCalendarId));
			jsonResp = response.getEntity();
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, CalendarEvents.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
	}

	@Override
	public CalendarEvents queryNextEventWithTimeMin(Integer maxResult, String timeMin, String nextPageToken,
			String googleCalendarId) throws GoogleApiSDKException {
		String jsonResp;
		try {

			ApiResponse response = UtilsExecutor.request(
					new QueryNextEventWithTimeMin(accessToken, maxResult, timeMin, nextPageToken, googleCalendarId));
			jsonResp = response.getEntity();
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, CalendarEvents.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
	}

	private class QueryNextEventWithTimeMin extends GetGoogleCalendarApiReq {

		public QueryNextEventWithTimeMin(String accessToken, Integer maxResult, String timeMin, String nextPageToken,
				String googleCalendarId) throws Exception {
			super(accessToken,
					"calendars/" + googleCalendarId + "/events?maxResults=" + maxResult
							+ "&singleEvents=true&showDeleted=true&pageToken=" + nextPageToken + "&timeMin="
							+ URLEncoder.encode(timeMin, "UTF-8"));

		}

	}

	@Override
	public GoogleCalendarList getListCalendar() throws GoogleApiSDKException {
		String jsonResp;
		try {

			ApiResponse response = UtilsExecutor.request(new GetListCalendar(accessToken));
			jsonResp = response.getEntity();
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, GoogleCalendarList.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
	}

	private class GetListCalendar extends GetGoogleCalendarApiReq {

		public GetListCalendar(String accessToken) {
			super(accessToken, "users/me/calendarList?minAccessRole=owner");

		}

	}

}
