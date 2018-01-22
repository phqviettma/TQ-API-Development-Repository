package com.tq.calendar.req;

public class DeleteGoogleCalendarEventReq extends GenericGoogleCalendarReq{

	public DeleteGoogleCalendarEventReq(String accessToken, String resource) {
		super(accessToken, "DELETE", null, resource, null);
		
	}

	
}
