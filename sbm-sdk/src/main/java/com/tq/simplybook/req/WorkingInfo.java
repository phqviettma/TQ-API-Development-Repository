package com.tq.simplybook.req;

import java.io.Serializable;

public class WorkingInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -655665444993336299L;
	private String from;
	private String to;
	private Integer unitId;
	private Integer eventId;

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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	@Override
	public String toString() {
		return "BreakTime [from=" + from + ", to=" + to + ", unitId=" + unitId + ", eventId=" + eventId + "]";
	}

}
