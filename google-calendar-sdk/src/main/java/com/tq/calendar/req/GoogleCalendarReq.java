package com.tq.calendar.req;

public interface GoogleCalendarReq {
	final static String API_BASE_URL = "https://www.googleapis.com/calendar/v3";

	final static String CONTENT_TYPE = "application/json";

	String getAccessToken();

	Object getObject();

	String getHttpMethod();

	String getContentType();

	String getEndPoint();
}
