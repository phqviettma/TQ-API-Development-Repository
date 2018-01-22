package com.tq.common.lambda.dynamodb.service;

import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;

public interface SbmGoogleCalendarDbService extends BaseItemService<SbmGoogleCalendar, Long> {
	public void delete(SbmGoogleCalendar sbmGoogleCalendarSync);

	public SbmGoogleCalendar queryWithIndex(String eventId);
}
