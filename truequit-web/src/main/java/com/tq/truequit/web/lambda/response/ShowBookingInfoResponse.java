package com.tq.truequit.web.lambda.response;

import java.io.Serializable;
import java.util.List;

import com.tq.common.lambda.dynamodb.model.SbmBookingInfo;

public class ShowBookingInfoResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 541682562343707826L;
	private boolean succeeded;
	private List<SbmBookingInfo> bookingLists;
	private Integer total;

	public boolean isSucceeded() {
		return succeeded;
	}

	public void setSucceeded(boolean succeeded) {
		this.succeeded = succeeded;
	}

	public List<SbmBookingInfo> getBookingLists() {
		return bookingLists;
	}

	public void setBookingLists(List<SbmBookingInfo> bookingLists) {
		this.bookingLists = bookingLists;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "ShowBookingInfoResponse [succeeded=" + succeeded + ", bookingLists=" + bookingLists + ", total=" + total
				+ "]";
	}

	public ShowBookingInfoResponse(boolean succeeded, List<SbmBookingInfo> bookingLists, Integer total) {

		this.succeeded = succeeded;
		this.bookingLists = bookingLists;
		this.total = total;
	}

	public ShowBookingInfoResponse() {

	}

}
