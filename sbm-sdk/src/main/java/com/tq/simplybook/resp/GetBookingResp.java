package com.tq.simplybook.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetBookingResp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1178787127808991438L;
	private String id;
	private String record_date;
	private String start_date;
	private String end_date;
	private String unit_id;
	private String text;
	private String client;
	private String unit;
	private String unit_email;
	private String event;
	private String event_id;
	private String client_phone;
	private String client_email;
	private String is_confirm;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRecord_date() {
		return record_date;
	}

	public void setRecord_date(String record_date) {
		this.record_date = record_date;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit_email() {
		return unit_email;
	}

	public void setUnit_email(String unit_email) {
		this.unit_email = unit_email;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEvent_id() {
		return event_id;
	}

	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}

	public String getClient_phone() {
		return client_phone;
	}

	public void setClient_phone(String client_phone) {
		this.client_phone = client_phone;
	}

	public String getClient_email() {
		return client_email;
	}

	public void setClient_email(String client_email) {
		this.client_email = client_email;
	}

	public String getIs_confirm() {
		return is_confirm;
	}

	public void setIs_confirm(String is_confirm) {
		this.is_confirm = is_confirm;
	}

	@Override
	public String toString() {
		return "GetBookingResp [id=" + id + ", record_date=" + record_date + ", start_date=" + start_date
				+ ", end_date=" + end_date + ", unit_id=" + unit_id + ", text=" + text + ", client=" + client
				+ ", unit=" + unit + ", unit_email=" + unit_email + ", event=" + event + ", event_id=" + event_id
				+ ", client_phone=" + client_phone + ", client_email=" + client_email + ", is_confirm=" + is_confirm
				+ "]";
	}

}
