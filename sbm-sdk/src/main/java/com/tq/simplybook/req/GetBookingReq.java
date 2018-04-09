package com.tq.simplybook.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetBookingReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3352562819153893570L;
	private String date_from;
	private String booking_type;
	private String order;
	private Integer unit_group_id;
	private Integer event_id;

	public GetBookingReq(String date_from, String booking_type, String order, Integer unit_group_id, Integer event_id) {

		this.date_from = date_from;
		this.booking_type = booking_type;
		this.order = order;
		this.unit_group_id = unit_group_id;
		this.event_id = event_id;
	}

	public GetBookingReq() {

	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getDate_from() {
		return date_from;
	}

	public void setDate_from(String date_from) {
		this.date_from = date_from;
	}

	public String getBooking_type() {
		return booking_type;
	}

	public void setBooking_type(String booking_type) {
		this.booking_type = booking_type;
	}

	@Override
	public String toString() {
		return "GetBookingReq [date_from=" + date_from + ", booking_type=" + booking_type + ", order=" + order
				+ ", unit_group_id=" + unit_group_id + ", event_id=" + event_id + "]";
	}

	public Integer getUnit_group_id() {
		return unit_group_id;
	}

	public void setUnit_group_id(Integer unit_group_id) {
		this.unit_group_id = unit_group_id;
	}

	public Integer getEvent_id() {
		return event_id;
	}

	public void setEvent_id(Integer event_id) {
		this.event_id = event_id;
	}

}
