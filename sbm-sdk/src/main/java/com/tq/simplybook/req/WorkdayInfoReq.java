package com.tq.simplybook.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkdayInfoReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4024440241759976781L;
	private String from;
	private String to;
	private int unit_id;
	private int event_id;

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


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public WorkdayInfoReq(String from, String to, int unit_id, int event_id) {

		this.from = from;
		this.to = to;
		this.unit_id = unit_id;
		this.event_id = event_id;
		
	}

	public WorkdayInfoReq() {

	}

}
