package com.tq.simplybook.req;

import java.io.Serializable;

public class GetWorkDayInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 706601627544073222L;
	
	private String from;
	private String to;
	private String unitId;
	private String eventId;
	private String count;
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
}
