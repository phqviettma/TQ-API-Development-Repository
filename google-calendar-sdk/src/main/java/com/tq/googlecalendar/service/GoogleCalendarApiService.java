package com.tq.googlecalendar.service;

import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.req.EventReq;
import com.tq.googlecalendar.req.StopWatchEventReq;
import com.tq.googlecalendar.req.WatchEventReq;
import com.tq.googlecalendar.resp.CalendarEvents;
import com.tq.googlecalendar.resp.ErrorResp;
import com.tq.googlecalendar.resp.EventResp;
import com.tq.googlecalendar.resp.GoogleCalendarList;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.resp.WatchEventResp;

public interface GoogleCalendarApiService {

	EventResp createEvent(EventReq events,String googleCalendarId) throws GoogleApiSDKException;

	boolean deleteEvent(String eventId, String googleCalendarId) throws GoogleApiSDKException;

	EventResp getEvent(String eventId, String googleCalendarId) throws GoogleApiSDKException;

	WatchEventResp watchEvent(WatchEventReq req, String googleCalendarId) throws GoogleApiSDKException;

	CalendarEvents getEventWithNextPageToken(Integer maxResult, String syncToken, String pageToken,String googleCalendarId)
			throws GoogleApiSDKException;

	CalendarEvents getEventWithNextSyncToken(Integer maxResult, String syncToken,String googleCalendarId) throws GoogleApiSDKException;

	CalendarEvents getEventWithoutToken(Integer maxResult, String query, String timeMin, String googleCalendarId) throws GoogleApiSDKException;

	GoogleCalendarSettingsInfo getSettingInfo(String settingId) throws GoogleApiSDKException;

	CalendarEvents getUpdatedEventWithPageToken(Integer maxResult, String lastQueryTimeMin,String nextPageToken,String googleCalendarId) throws GoogleApiSDKException;

	ErrorResp stopWatchEvent(StopWatchEventReq stopEventReq) throws GoogleApiSDKException;

	CalendarEvents queryNewestEvent(Integer maxResult, String time, String googleCalendarId) throws GoogleApiSDKException;

	CalendarEvents queryNextEventWithTimeMin(Integer maxResult, String timeMin, String nextPageToken, String googleCalendarId)
			throws GoogleApiSDKException;
	
	GoogleCalendarList getListCalendar() throws GoogleApiSDKException;

}
