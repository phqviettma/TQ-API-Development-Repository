package com.tq.simplybook.resp;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditBookData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1426116267034809230L;
	
	private String require_confirm;
	
	private List<BookResp> bookings;

	public String getRequire_confirm() {
		return require_confirm;
	}

	public void setRequire_confirm(String require_confirm) {
		this.require_confirm = require_confirm;
	}

	public List<BookResp> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookResp> bookings) {
		this.bookings = bookings;
	}

	@Override
	public String toString() {
		return "EditBookData [require_confirm=" + require_confirm + ", bookings=" + bookings + "]";
	}
	
}
