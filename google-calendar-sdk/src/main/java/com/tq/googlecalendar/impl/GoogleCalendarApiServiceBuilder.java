package com.tq.googlecalendar.impl;

import com.tq.googlecalendar.service.GoogleCalendarApiService;

public class GoogleCalendarApiServiceBuilder {
	public GoogleCalendarApiService build(String accessToken) {
		return new GoogleCalendarApiServiceImpl(accessToken);
	}
}
