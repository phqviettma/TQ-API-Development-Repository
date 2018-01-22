package com.tq.calendar.req;

public class GetGoogleCalendarApiReq extends GenericGoogleCalendarReq{

	public GetGoogleCalendarApiReq(String accessToken, String resource) {
		super(accessToken, "GET", "application/json", resource, null);
	
	}

	
	
	
}
