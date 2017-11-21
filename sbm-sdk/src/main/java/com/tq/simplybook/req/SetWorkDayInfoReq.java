package com.tq.simplybook.req;

import java.io.Serializable;

public class SetWorkDayInfoReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7583292045562267479L;
	
	private SetWorkDayInfoInfoReq info;
	
	public SetWorkDayInfoReq(SetWorkDayInfoInfoReq info) {
		this.info = info;
	}

	public SetWorkDayInfoInfoReq getInfo() {
		return info;
	}

	public void setInfo(SetWorkDayInfoInfoReq info) {
		this.info = info;
	}
	
	
	
}
