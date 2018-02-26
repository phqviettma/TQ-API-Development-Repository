package com.tq.common.lambda.dynamodb.service;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;

public interface GoogleCalendarDbService extends BaseItemService<GoogleCalendarSbmSync, String> {
	public GoogleCalendarSbmSync query(String email);

	public void delete(GoogleCalendarSbmSync googleCalendarSbmSync);
}
