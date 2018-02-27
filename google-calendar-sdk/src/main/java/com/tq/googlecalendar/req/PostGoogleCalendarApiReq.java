package com.tq.googlecalendar.req;

public class PostGoogleCalendarApiReq extends GenericGoogleCalendarReq{

	public PostGoogleCalendarApiReq(String accessToken, String resource,
			Object object) {
		super(accessToken, "POST", "application/json", resource, object);
	
	}

	


	



}
