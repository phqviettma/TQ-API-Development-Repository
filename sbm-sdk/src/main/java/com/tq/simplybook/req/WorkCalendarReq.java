package com.tq.simplybook.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkCalendarReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5115143015805656745L;
	private int year;
	private int month;
	private int unitId;
	@JsonProperty("event_id")
	private int eventId;
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	@Override
	public String toString() {
		return "WorkCalendarReq [year=" + year + ", month=" + month + ", unitId=" + unitId + ", eventId=" + eventId
				+ "]";
	}
}
