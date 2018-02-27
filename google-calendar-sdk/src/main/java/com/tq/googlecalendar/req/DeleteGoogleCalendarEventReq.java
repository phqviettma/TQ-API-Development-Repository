package com.tq.googlecalendar.req;

public class DeleteGoogleCalendarEventReq extends GenericGoogleCalendarReq{

	public DeleteGoogleCalendarEventReq(String accessToken, String resource) {
		super(accessToken, "DELETE", null, resource, null);
		
	}

	
}
