package com.tq.common.lambda.dynamodb.service;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;

public interface GoogleCalendarDbService extends BaseItemService<GoogleCalendarSbmSync, String> {
	public List<GoogleCalendarSbmSync> queryEmail(String email);

	public void delete(GoogleCalendarSbmSync googleCalendarSbmSync);

	public void deleteGoogleItem(List<GoogleCalendarSbmSync> googleItems);
}
