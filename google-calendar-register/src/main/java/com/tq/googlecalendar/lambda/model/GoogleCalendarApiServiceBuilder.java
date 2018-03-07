package com.tq.googlecalendar.lambda.model;

import com.tq.googlecalendar.impl.GoogleCalendarApiServiceImpl;
import com.tq.googlecalendar.service.GoogleCalendarApiService;

public class GoogleCalendarApiServiceBuilder {
	public GoogleCalendarApiService build(String accessToken) {
		return new GoogleCalendarApiServiceImpl(accessToken);
	}
}
