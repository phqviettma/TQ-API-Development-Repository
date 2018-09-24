package com.tq.simplybook.req;

import java.io.Serializable;

public class SbmConfirmBooking implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7900790587407123104L;
	private Integer bookingId;
	private Integer statusId;

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public SbmConfirmBooking(Integer bookingId, Integer statusId) {
		this.bookingId = bookingId;
		this.statusId = statusId;
	}

	public SbmConfirmBooking() {

	}

}
