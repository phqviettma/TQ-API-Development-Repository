package com.tq.simplybook.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkSchedule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6606359560372400147L;
	
	private String from;
	private String to;
	
	@JsonProperty("is_day_off")
	private String dayOff;

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

	public String getDayOff() {
		return dayOff;
	}

	public void setDayOff(String dayOff) {
		this.dayOff = dayOff;
	}

	public boolean isDayOff() {
		return "1".equalsIgnoreCase(getDayOff());
	}

	@Override
	public String toString() {
		return "WorkSchedule [from=" + from + ", to=" + to + ", isDayOff=" + isDayOff() + "]";
	}

}
