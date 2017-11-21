package com.tq.simplybook.req;

import java.io.Serializable;

public class WorkdayReq implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = -8145369080902277411L;
	private WorkdayInfoReq workday;

	public WorkdayInfoReq getWorkday() {
		return workday;
	}

	public void setWorkday(WorkdayInfoReq workday) {
		this.workday = workday;
	}

	public WorkdayReq(WorkdayInfoReq workday) {
		
		this.workday = workday;
	}
	

}
