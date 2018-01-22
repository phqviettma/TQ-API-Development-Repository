package com.tq.common.lambda.dynamodb.dao;

import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.GenericItem;

public interface SbmGoogleCalendarSyncDao extends GenericItem<SbmGoogleCalendar, Long> {
	public SbmGoogleCalendar queryIndex(String eventId);
}
