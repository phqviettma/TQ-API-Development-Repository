package com.tq.simplybook.impl;

import java.io.Serializable;

import com.tq.simplybook.req.ClientData;

public class SbmBooking implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6439438547308125364L;
	private String eventId;
	private String unitId;
	private String clientId;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private Integer count;
	private ClientData clientData;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public ClientData getClientData() {
		return clientData;
	}

	public void setClientData(ClientData clientData) {
		this.clientData = clientData;
	}

	public SbmBooking(String eventId, String unitId, String clientId, String startDate, String startTime,
			String endDate, String endTime, Integer count, ClientData clientData) {

		this.eventId = eventId;
		this.unitId = unitId;
		this.clientId = clientId;
		this.startDate = startDate;
		this.startTime = startTime;
		this.endDate = endDate;
		this.endTime = endTime;
		this.count = count;
		this.clientData = clientData;
	}

}
