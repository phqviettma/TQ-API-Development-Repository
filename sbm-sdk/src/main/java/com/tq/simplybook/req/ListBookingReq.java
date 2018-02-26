package com.tq.simplybook.req;

import java.io.Serializable;
import java.util.List;

public class ListBookingReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5763884645266639379L;
	private List<GetBookingReq> params;

	public List<GetBookingReq> getParams() {
		return params;
	}

	public void setParams(List<GetBookingReq> params) {
		this.params = params;
	}

	public ListBookingReq(List<GetBookingReq> params) {

		this.params = params;
	}

	public ListBookingReq() {

	}

	

}
