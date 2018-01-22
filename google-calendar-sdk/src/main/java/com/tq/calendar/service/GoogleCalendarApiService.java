package com.tq.calendar.service;

import com.tq.calendar.exception.GoogleApiSDKException;
import com.tq.calendar.req.EventReq;
import com.tq.calendar.req.WatchEventReq;
import com.tq.calendar.resp.CalendarEvents;
import com.tq.calendar.resp.EventResp;
import com.tq.calendar.resp.GoogleCalendarSettingsInfo;
import com.tq.calendar.resp.WatchEventResp;

public interface GoogleCalendarApiService {

	EventResp createEvent(EventReq events) throws GoogleApiSDKException;

	boolean deleteEvent(String eventId) throws GoogleApiSDKException;

	EventResp getEvent(String eventId) throws GoogleApiSDKException;

	WatchEventResp watchEvent(WatchEventReq req,String email) throws GoogleApiSDKException;

	CalendarEvents getEventWithNextPageToken(Integer maxResult, String syncToken, String pageToken) throws GoogleApiSDKException;
	
	CalendarEvents getEventWithNextSyncToken(Integer maxResult, String syncToken) throws GoogleApiSDKException;
	
	CalendarEvents getEventWithoutToken(Integer maxResult, String timeMin) throws GoogleApiSDKException;

	GoogleCalendarSettingsInfo getSettingInfo(String settingId) throws GoogleApiSDKException;

	CalendarEvents getEventAtLastTime(Integer maxResult, String lastQueryTimeMin, String nextPageToken) throws GoogleApiSDKException;
}
