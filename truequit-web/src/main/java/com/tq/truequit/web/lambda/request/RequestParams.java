package com.tq.truequit.web.lambda.request;

import java.io.Serializable;

public class RequestParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6052598810305407037L;
	private int size;
	private String email;
	private Integer bookingId;
	private String clientEmail;
	private Long timeStamp;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public String getClientEmail() {
		return clientEmail;
	}

	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "RequestParams [size=" + size + ", email=" + email + ", bookingId=" + bookingId + ", clientEmail="
				+ clientEmail + ", timeStamp=" + timeStamp + "]";
	}

}
