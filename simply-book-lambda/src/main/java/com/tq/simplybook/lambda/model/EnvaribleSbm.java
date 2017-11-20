package com.tq.simplybook.lambda.model;

import java.io.Serializable;

import com.tq.simplybook.lambda.context.Env;

public class EnvaribleSbm implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -883684229692087173L;

	private Env m_Env;
	
	private String companyLogin = m_Env.getSimplyBookCompanyLogin();
	private String user = m_Env.getSimplyBookUser();
	private String password = m_Env.getSimplyBookPassword();
	private String loginEndPoint = m_Env.getSimplyBookServiceUrlLogin();
	private String adminEndPoint = m_Env.getSimplyBookAdminServiceUrl();
	private String infusionSoftApiKey = m_Env.getInfusionSoftApiKey();
	private String infusionSoftApiName = m_Env.getInfusionSoftApiName();
	private String clinikoKey = m_Env.getClinikoApiKey();
	
	public String getCompanyLogin() {
		return companyLogin;
	}
	public void setCompanyLogin(String companyLogin) {
		this.companyLogin = companyLogin;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLoginEndPoint() {
		return loginEndPoint;
	}
	public void setLoginEndPoint(String loginEndPoint) {
		this.loginEndPoint = loginEndPoint;
	}
	public String getAdminEndPoint() {
		return adminEndPoint;
	}
	public void setAdminEndPoint(String adminEndPoint) {
		this.adminEndPoint = adminEndPoint;
	}
	
	public String getInfusionSoftApiKey() {
		return infusionSoftApiKey;
	}
	public void setInfusionSoftApiKey(String infusionSoftApiKey) {
		this.infusionSoftApiKey = infusionSoftApiKey;
	}
	public String getInfusionSoftApiName() {
		return infusionSoftApiName;
	}
	public void setInfusionSoftApiName(String infusionSoftApiName) {
		this.infusionSoftApiName = infusionSoftApiName;
	}
	public String getClinikoKey() {
		return clinikoKey;
	}
	public void setClinikoKey(String clinikoKey) {
		this.clinikoKey = clinikoKey;
	}
	
}
