package com.tq.googlecalendar.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tq.googlecalendar.resp.End;
import com.tq.googlecalendar.resp.Start;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventReq implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 7189505301920616679L;
	private Start start;
	private End end;
	private String description;
	private String summary;
	

	public Start getStart() {
		return start;
	}

	public void setStart(Start start) {
		this.start = start;
	}

	public End getEnd() {
		return end;
	}

	public void setEnd(End end) {
		this.end = end;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	

	public EventReq(Start start, End end, String description, String summary) {
		this.start = start;
		this.end = end;
		this.description = description;
		this.summary = summary;
	}

	public EventReq() {

	}

}
