package com.tq.simplybook.resp;

import java.io.Serializable;
import java.util.Set;

public class WorksDayInfoResp implements Serializable {
	private static final long serialVersionUID = 4901190403552351432L;
	
	private String date;
	
	private Set<WorkTimeSlot> info;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Set<WorkTimeSlot> getInfo() {
		return info;
	}

	public void setInfo(Set<WorkTimeSlot> info) {
		this.info = info;
	}

	
}
