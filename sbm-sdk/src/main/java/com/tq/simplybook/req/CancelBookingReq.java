package com.tq.simplybook.req;

import java.io.Serializable;

public class CancelBookingReq implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3280045055384159279L;
	Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CancelBookingReq(Integer id) {
	
		this.id = id;
	}

	public CancelBookingReq() {
	
	}
	
}
