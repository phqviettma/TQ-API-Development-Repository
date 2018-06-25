package com.tq.simplybook.resp;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@DynamoDBDocument
public class GetBookingResp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1178787127808991438L;
	private String id;
	private String start_date;
	private String end_date;
	private String phone;
	private String client;

	private String event;
	private String client_email;

	@DynamoDBAttribute(attributeName = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName = "startDate")
	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	@DynamoDBAttribute(attributeName = "endDate")
	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	@DynamoDBAttribute(attributeName = "clientName")
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@DynamoDBAttribute(attributeName = "clientEmail")
	public String getClient_email() {
		return client_email;
	}

	public void setClient_email(String client_email) {
		this.client_email = client_email;
	}

	public GetBookingResp withId(String id) {
		this.setId(id);
		return this;
	}

	@DynamoDBAttribute(attributeName = "phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "GetBookingResp [id=" + id + ", start_date=" + start_date + ", end_date=" + end_date + ", client="
				+ client + ", event=" + event + ", client_email=" + client_email + "]";
	}

	public GetBookingResp(String id, String start_date, String end_date, String client, String event,
			String client_email, String phone) {

		this.id = id;
		this.start_date = start_date;
		this.end_date = end_date;
		this.client = client;
		this.event = event;
		this.client_email = client_email;
		this.phone = phone;
	}

	public GetBookingResp() {
	}

}
