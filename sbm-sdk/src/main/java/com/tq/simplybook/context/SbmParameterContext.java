package com.tq.simplybook.context;

public class SbmParameterContext {

	private String companyLogin;
	private String endpoint;
	private String userToken;
	private int eventId;
	private int unitId;
	private String from;
	private String to;
	private String startBreakTime;
	private String endBreakTime;

	public String getFrom() {
		return from;
	}

	public SbmParameterContext setFrom(String from) {
		this.from = from;
		return this;
	}

	public String getTo() {
		return to;
	}

	public SbmParameterContext setTo(String to) {
		this.to = to;
		return this;
	}

	public String getCompanyLogin() {
		return companyLogin;
	}

	public SbmParameterContext setCompanyLogin(String companyLogin) {
		this.companyLogin = companyLogin;
		return this;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public SbmParameterContext setEndpoint(String endpoint) {
		this.endpoint = endpoint;
		return this;
	}

	public String getUserToken() {
		return userToken;
	}

	public SbmParameterContext setUserToken(String userToken) {
		this.userToken = userToken;
		return this;
	}

	public int getEventId() {
		return eventId;
	}

	public SbmParameterContext setEventId(int eventId) {
		this.eventId = eventId;
		return this;
	}

	public int getUnitId() {
		return unitId;
	}

	public SbmParameterContext setUnitId(int unitId) {
		this.unitId = unitId;
		return this;
	}

	public String getStartBreakTime() {
		return startBreakTime;
	}

	public SbmParameterContext setStartBreakTime(String startBreakTime) {
		this.startBreakTime = startBreakTime;
		return this;
	}

	public String getEndBreakTime() {
		return endBreakTime;
	}

	public SbmParameterContext setEndBreakTime(String endBreakTime) {
		this.endBreakTime = endBreakTime;
		return this;
	}
}
