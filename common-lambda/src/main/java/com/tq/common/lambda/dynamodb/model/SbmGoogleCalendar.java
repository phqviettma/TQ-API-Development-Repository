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
	private Integer flag;
	private String agent;
	private String googleEmail;

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

	@DynamoDBAttribute(attributeName = "agent")
	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}
	@DynamoDBAttribute(attributeName = "googleEmail")
	public String getGoogleEmail() {
		return googleEmail;
	}

	public void setGoogleEmail(String googleEmail) {
		this.googleEmail = googleEmail;
	}



	public SbmGoogleCalendar(Long sbmId, String eventId, String clientEmail, Integer flag, String checkKind) {

		this.sbmId = sbmId;
		this.eventId = eventId;
		this.clientEmail = clientEmail;
		this.flag = flag;
		this.agent = checkKind;
	}

	public SbmGoogleCalendar(Long sbmId, String eventId, Integer flag, String agent,
			String googleEmail) {
		this.sbmId = sbmId;
		this.eventId = eventId;
		this.flag = flag;
		this.agent = agent;
		this.googleEmail = googleEmail;
	}

	public SbmGoogleCalendar() {

	}

	@DynamoDBAttribute(attributeName = "flag")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "SbmGoogleCalendar [sbmId=" + sbmId + ", eventId=" + eventId + ", clientEmail=" + clientEmail + ", flag="
				+ flag + ", agent=" + agent + "]";
	}

}
