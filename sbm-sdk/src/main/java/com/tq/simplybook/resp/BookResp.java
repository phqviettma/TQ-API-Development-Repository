package com.tq.simplybook.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3371566177087613096L;
	
	private Integer eventId;
	private Integer clientId;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private Integer clientTimeOffset;
	private Object additional;
	
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getClientTimeOffset() {
		return clientTimeOffset;
	}
	public void setClientTimeOffset(Integer clientTimeOffset) {
		this.clientTimeOffset = clientTimeOffset;
	}
	public Object getAdditional() {
		return additional;
	}
	public void setAdditional(Object additional) {
		this.additional = additional;
	}
	@Override
	public String toString() {
		return "BookResp [eventId=" + eventId + ", clientId=" + clientId + ", startDate=" + startDate + ", startTime="
				+ startTime + ", endDate=" + endDate + ", endTime=" + endTime + ", clientTimeOffset=" + clientTimeOffset
				+ ", additional=" + additional + "]";
	}
}
