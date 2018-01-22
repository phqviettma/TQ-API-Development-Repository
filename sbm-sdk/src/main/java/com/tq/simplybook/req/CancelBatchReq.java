package com.tq.simplybook.req;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CancelBatchReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6110696598762621651L;
	private String id;
	private List<Long> bookingIds;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Long> getBookingIds() {
		return bookingIds;
	}

	public void setBookingIds(List<Long> bookingIds) {
		this.bookingIds = bookingIds;
	}

	@Override
	public String toString() {
		return "CancelBatchReq [id=" + id + ", bookingIds=" + bookingIds + "]";
	}

	public CancelBatchReq(String s, List<Long> bookingIds) {
		this.id = s;
		this.bookingIds = bookingIds;
	}

}
