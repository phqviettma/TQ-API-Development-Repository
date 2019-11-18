package com.tq.simplybook.resp;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkCalendarResp implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5456174903310029072L;
	
	private Map<String, WorkSchedule> result;
	public Map<String, WorkSchedule> getResult() {
		return result;
	}
	public void setResult(Map<String, WorkSchedule> result) {
		this.result = result;
	}
	
	
	@Override
	public String toString() {
		return "WorkCalendarResp [result=" + result + "]";
	}
}
