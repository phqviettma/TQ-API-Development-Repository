package com.tq.simplybook.test;

import java.io.Serializable;

public class WorkdayInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4024440241759976781L;
	private String from;
	private String to;
	private int unit_id;
	private int event_id;
	private int count;

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

	public int getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(int unit_id) {
		this.unit_id = unit_id;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public WorkdayInfo(String from, String to, int unit_id, int event_id, int count) {

		this.from = from;
		this.to = to;
		this.unit_id = unit_id;
		this.event_id = event_id;
		this.count = count;
	}

	public WorkdayInfo() {

	}

}
