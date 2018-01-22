package com.tq.common.lambda.dynamodb.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "SbmGoogleCalendar")
public class SbmGoogleCalendar implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4416696890647126245L;
	private Long sbmId;
	private String eventId;
	private String clientEmail;

	@DynamoDBAttribute(attributeName = "clientEmail")
	public String getClientEmail() {
		return clientEmail;
	}

	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}

	@DynamoDBHashKey(attributeName = "sbmId")
	public Long getSbmId() {
		return sbmId;
	}

	public void setSbmId(Long sbmId) {
		this.sbmId = sbmId;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "Event-Index", attributeName = "eventId")
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public SbmGoogleCalendar(Long sbmId, String eventId, String clientEmail) {

		this.sbmId = sbmId;
		this.eventId = eventId;
		this.clientEmail = clientEmail;
	}

	public SbmGoogleCalendar() {

	}

	@Override
	public String toString() {
		return "SbmGoogleCalendar [sbmId=" + sbmId + ", eventId=" + eventId + ", clientEmail=" + clientEmail + "]";
	}

}
