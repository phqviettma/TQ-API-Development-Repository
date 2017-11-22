package com.tq.simplybook.context;

import java.util.List;

import com.tq.simplybook.resp.Breaktime;

public class SbmParameterContext {

	private String companyLogin;
	private String endpoint;
	private String userToken;
	private int eventId;
	private int unitId;
	private String envStartWorkingTime;
	private String envEndWorkingTime;
	private List<Breaktime> breakTimes;
	
	public String getCompanyLogin() {
		return companyLogin;
	}
	public void setCompanyLogin(String companyLogin) {
		this.companyLogin = companyLogin;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public String getEnvStartWorkingTime() {
		return envStartWorkingTime;
	}
	public void setEnvStartWorkingTime(String envStartWorkingTime) {
		this.envStartWorkingTime = envStartWorkingTime;
	}
	public String getEnvEndWorkingTime() {
		return envEndWorkingTime;
	}
	public void setEnvEndWorkingTime(String envEndWorkingTime) {
		this.envEndWorkingTime = envEndWorkingTime;
	}
	public List<Breaktime> getBreakTimes() {
		return breakTimes;
	}
	public void setBreakTimes(List<Breaktime> breakTimes) {
		this.breakTimes = breakTimes;
	}
}
