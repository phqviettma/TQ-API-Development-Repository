package com.tq.simplybook.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingInfo implements Serializable {
	private static final long serialVersionUID = -5324059681740376947L;

	private Integer is_confirmed;
	private String comment;
	private String event_id;
	private String event_name;
	private String event_duration;
	private String unit_id;
	private String unit_name;
	private String unit_email;
	private String unit_description;
	private String start_date_time;
	private String end_date_time;
	private String record_date;
	private Integer client_id;
	private String client_name;
	private String client_phone;
	private String client_email;
	private String client_address1;
	private String client_address2;
	private String client_city;
	private String client_zip;
	private String client_country_id;
	private String company_login;
	private String company_name;
	private Location location;

	public Integer getIs_confirmed() {
		return is_confirmed;
	}

	public String getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}

	public void setIs_confirmed(Integer is_confirmed) {
		this.is_confirmed = is_confirmed;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEvent_name() {
		return event_name;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	public String getEvent_duration() {
		return event_duration;
	}

	public void setEvent_duration(String event_duration) {
		this.event_duration = event_duration;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	public String getUnit_description() {
		return unit_description;
	}

	public void setUnit_description(String unit_description) {
		this.unit_description = unit_description;
	}

	public String getStart_date_time() {
		return start_date_time;
	}

	public void setStart_date_time(String start_date_time) {
		this.start_date_time = start_date_time;
	}

	public String getEnd_date_time() {
		return end_date_time;
	}

	public void setEnd_date_time(String end_date_time) {
		this.end_date_time = end_date_time;
	}

	public String getRecord_date() {
		return record_date;
	}

	public void setRecord_date(String record_date) {
		this.record_date = record_date;
	}

	public Integer getClient_id() {
		return client_id;
	}

	public void setClient_id(Integer client_id) {
		this.client_id = client_id;
	}

	public String getEvent_id() {
		return event_id;
	}

	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	public String getUnit_email() {
		return unit_email;
	}

	public void setUnit_email(String unit_email) {
		this.unit_email = unit_email;
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

	public String getClient_address1() {
		return client_address1;
	}

	public void setClient_address1(String client_address1) {
		this.client_address1 = client_address1;
	}

	public String getClient_address2() {
		return client_address2;
	}

	public void setClient_address2(String client_address2) {
		this.client_address2 = client_address2;
	}

	public String getClient_city() {
		return client_city;
	}

	public void setClient_city(String client_city) {
		this.client_city = client_city;
	}

	public String getClient_zip() {
		return client_zip;
	}

	public void setClient_zip(String client_zip) {
		this.client_zip = client_zip;
	}

	public String getClient_country_id() {
		return client_country_id;
	}

	public void setClient_country_id(String client_country_id) {
		this.client_country_id = client_country_id;
	}

	public String getCompany_login() {
		return company_login;
	}

	public void setCompany_login(String company_login) {
		this.company_login = company_login;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public BookingInfo withClientName(String clientName) {
		this.setClient_name(clientName);
		return this;
	}

	public BookingInfo withClientPhone(String clientPhone) {
		this.setClient_phone(clientPhone);
		return this;
	}

	public BookingInfo withClientEmail(String clientEmail) {
		this.setClient_email(clientEmail);
		return this;

	}

	public BookingInfo withClientId(Integer clientId) {
		this.setClient_id(clientId);
		return this;
	}

	public BookingInfo withRecordDate(String recordDate) {
		this.setRecord_date(recordDate);
		return this;
	}

	@Override
	public String toString() {
		return "BookingInfo [is_confirmed=" + is_confirmed + ", comment=" + comment + ", event_id=" + event_id
				+ ", event_name=" + event_name + ", event_duration=" + event_duration + ", unit_id=" + unit_id
				+ ", unit_name=" + unit_name + ", unit_email=" + unit_email + ", unit_description=" + unit_description
				+ ", start_date_time=" + start_date_time + ", end_date_time=" + end_date_time + ", record_date="
				+ record_date + ", client_id=" + client_id + ", client_name=" + client_name + ", client_phone="
				+ client_phone + ", client_email=" + client_email + ", client_address1=" + client_address1
				+ ", client_address2=" + client_address2 + ", client_city=" + client_city + ", client_zip=" + client_zip
				+ ", client_country_id=" + client_country_id + ", company_login=" + company_login + ", company_name="
				+ company_name + ", location=" + location + "]";
	}

}
