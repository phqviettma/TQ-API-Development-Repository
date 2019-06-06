package com.tq.simplybook.req;

import java.io.Serializable;

public class EditBookReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3953232270432764703L;

	private Integer shedulerId;
	private Integer eventId;
	private Integer unitId;
	private Integer clientId;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private Integer clientTimeOffset;
	private Object additional;
	
	public EditBookReq(Integer schedulerId, Integer eventId, Integer unitId, Integer clientId, String startDate, String startTime, String endDate, String endTime, Integer clientTimeOffset) {
		this.shedulerId = schedulerId;
		this.eventId = eventId;
		this.unitId = unitId;
		this.clientId = clientId;
		this.startDate = startDate;
		this.startTime = startTime;
		this.endDate = endDate;
		this.endTime = endTime;
		this.clientTimeOffset = clientTimeOffset;
		this.additional = "";
	}
	
	@Override
	public String toString() {
		return "EditBookReq [shedulerId=" + shedulerId + ", eventId=" + eventId + ", unitId=" + unitId + ", clientId="
				+ clientId + ", startDate=" + startDate + ", startTime=" + startTime + ", endDate=" + endDate
				+ ", endTime=" + endTime + ", clientTimeOffset=" + clientTimeOffset + ", additional=" + additional
				+ "]";
	}

	public EditBookReq() {}
	
	public Integer getShedulerId() {
		return shedulerId;
	}
	public void setShedulerId(Integer shedulerId) {
		this.shedulerId = shedulerId;
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
	
	
}
