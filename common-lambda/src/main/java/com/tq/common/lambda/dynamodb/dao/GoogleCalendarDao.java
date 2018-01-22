package com.tq.common.lambda.dynamodb.dao;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.GenericItem;

public interface GoogleCalendarDao extends GenericItem<GoogleCalendarSbmSync, String> {
	public GoogleCalendarSbmSync queryIndex(String email);
	
}
