package com.tq.simplybook.resp;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitWorkingTime implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7084816113485220432L;
	private String date;
	private Map<String, WorkingTime> workingTime;

	public Map<String, WorkingTime> getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(Map<String, WorkingTime> workingTime) {
		this.workingTime = workingTime;
	}

	public UnitWorkingTime() {

	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public UnitWorkingTime(String date, Map<String, WorkingTime> workingTime) {

		this.date = date;
		this.workingTime = workingTime;
	}

	@Override
	public String toString() {
		return "UnitWorkingTime [date=" + date + ", workingTime=" + workingTime + "]";
	}

}
