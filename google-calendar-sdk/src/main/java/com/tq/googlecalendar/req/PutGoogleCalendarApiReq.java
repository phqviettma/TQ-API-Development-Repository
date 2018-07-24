package com.tq.googlecalendar.req;

public class PutGoogleCalendarApiReq extends GenericGoogleCalendarReq {
	public PutGoogleCalendarApiReq(String accessToken, String resource, Object object) {
		super(accessToken, "PUT", "application/json", resource, object);

	}
}
