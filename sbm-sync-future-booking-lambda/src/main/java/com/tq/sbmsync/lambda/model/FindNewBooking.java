package com.tq.sbmsync.lambda.model;

import java.util.List;

import com.tq.simplybook.resp.GetBookingResp;

public class FindNewBooking {
	private int count;
	private List<GetBookingResp> bookingList;

	public int getCount() {
		return count;
	}

	public List<GetBookingResp> getBookingList() {
		return bookingList;
	}

	public FindNewBooking(int count, List<GetBookingResp> bookingList) {
		this.count = count;
		this.bookingList = bookingList;
	}
}
