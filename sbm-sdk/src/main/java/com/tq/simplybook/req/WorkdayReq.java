package com.tq.simplybook.req;

import java.io.Serializable;

import com.tq.simplybook.test.WorkdayInfo;

public class WorkdayReq implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = -8145369080902277411L;
	private WorkdayInfo workday;

	public WorkdayInfo getWorkday() {
		return workday;
	}

	public void setWorkday(WorkdayInfo workday) {
		this.workday = workday;
	}

	public WorkdayReq(WorkdayInfo workday) {
		
		this.workday = workday;
	}
	

}
