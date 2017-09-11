package com.tq.simplybook.req;

import java.io.Serializable;
import java.util.List;

import com.tq.simplybook.resp.Workday;
import com.tq.simplybook.resp.WorkingDay;

public class SpecialDayReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2356516494272177281L;
	private Workday workingDay;
	private String date;

	public Workday getWorkingDay() {
		return workingDay;
	}

	public void setWorkingDay(Workday workingDay) {
		this.workingDay = workingDay;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "SpecialDayReq [workingDay=" + workingDay + ", date=" + date + "]";
	}

}
