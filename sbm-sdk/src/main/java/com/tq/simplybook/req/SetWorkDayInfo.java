package com.tq.simplybook.req;

import java.io.Serializable;

import com.tq.simplybook.resp.DayInfo;

public class SetWorkDayInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7583292045562267479L;
	
	private DayInfo info;
	
	public SetWorkDayInfo(DayInfo info) {
		this.info = info;
	}

	public DayInfo getInfo() {
		return info;
	}

	public void setInfo(DayInfo info) {
		this.info = info;
	}
	
	
	
}
