package com.tq.googlecalendar.service;

import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.req.EventReq;
import com.tq.googlecalendar.req.StopWatchEventReq;
import com.tq.googlecalendar.req.WatchEventReq;
import com.tq.googlecalendar.resp.CalendarEvents;
import com.tq.googlecalendar.resp.EventResp;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.resp.WatchEventResp;

public interface GoogleCalendarApiService {

	EventResp createEvent(EventReq events) throws GoogleApiSDKException;

	boolean deleteEvent(String eventId) throws GoogleApiSDKException;

	EventResp getEvent(String eventId) throws GoogleApiSDKException;

	WatchEventResp watchEvent(WatchEventReq req, String email) throws GoogleApiSDKException;

	CalendarEvents getEventWithNextPageToken(Integer maxResult, String syncToken, String pageToken)
			throws GoogleApiSDKException;

	CalendarEvents getEventWithNextSyncToken(Integer maxResult, String syncToken) throws GoogleApiSDKException;

	CalendarEvents getEventWithoutToken(Integer maxResult, String timeMin) throws GoogleApiSDKException;

	GoogleCalendarSettingsInfo getSettingInfo(String settingId) throws GoogleApiSDKException;

	CalendarEvents getEventAtLastTime(Integer maxResult, String lastQueryTimeMin, String nextPageToken)
			throws GoogleApiSDKException;

	boolean stopWatchEvent(StopWatchEventReq stopEventReq) throws GoogleApiSDKException;

}
