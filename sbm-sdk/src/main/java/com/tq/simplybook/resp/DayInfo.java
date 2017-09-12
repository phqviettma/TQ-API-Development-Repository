package com.tq.simplybook.resp;

import java.io.Serializable;
import java.util.List;

public class DayInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7450677689307116658L;
	private String start_time;
	private String end_time;
	private int is_day_off;
	private List<Breaktime> breaktime;
	private int index;
	private String name;
	private String date;
	private String unit_group_id;
	private String event_id;

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public int getIs_day_off() {
		return is_day_off;
	}

	public void setIs_day_off(int is_day_off) {
		this.is_day_off = is_day_off;
	}

	public List<Breaktime> getBreaktime() {
		return breaktime;
	}

	public void setBreaktime(List<Breaktime> breaktime) {
		this.breaktime = breaktime;
	}

	
	public DayInfo(String start_time, String end_time, int is_day_off, List<Breaktime> breaktime, int index,
			String name, String date, String unit_group_id, String event_id) {
		super();
		this.start_time = start_time;
		this.end_time = end_time;
		this.is_day_off = is_day_off;
		this.breaktime = breaktime;
		this.index = index;
		this.name = name;
		this.date = date;
		this.unit_group_id = unit_group_id;
		this.event_id = event_id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUnit_group_id() {
		return unit_group_id;
	}

	public void setUnit_group_id(String unit_group_id) {
		this.unit_group_id = unit_group_id;
	}

	public String getEvent_id() {
		return event_id;
	}

	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public DayInfo() {
		super();
	}
	
}
