package com.tq.simplybook.resp;

import java.io.Serializable;
import java.util.Set;

public class WorksDayInfo implements Serializable {
	private static final long serialVersionUID = 4901190403552351432L;
	
	private String date;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Set<TimeInfo> getInfo() {
		return info;
	}

	public void setInfo(Set<TimeInfo> info) {
		this.info = info;
	}

	private Set<TimeInfo> info;
	
}
