package com.tq.common.lambda.dynamodb.dao;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.GenericItem;

public interface GoogleCalendarDao extends GenericItem<GoogleCalendarSbmSync, String> {
	public List<GoogleCalendarSbmSync> queryEmail(String email);

	public void deleteGoogleItem(List<GoogleCalendarSbmSync> googleItem);
}
