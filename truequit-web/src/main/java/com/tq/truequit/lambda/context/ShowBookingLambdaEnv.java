package com.tq.truequit.lambda.context;

import com.amazonaws.regions.Regions;

public class ShowBookingLambdaEnv {
	private static ShowBookingLambdaEnv instance;
	private Regions regions = Regions.valueOf(System.getenv("REGIONS"));
	private String awsAccessKeyId = new String(System.getenv("AMAZON_ACCESS_KEY"));
	private String awsSecretAccessKey = new String(System.getenv("AMAZON_SECRET_ACCESS_KEY"));
	private String simplyBookServiceUrlLogin = System.getenv("SIMPLY_BOOK_SERVICE_URL_lOGIN");
	private String simplyBookAdminServiceUrl = System.getenv("SIMPLY_BOOK_ADMIN_SERVICE_URL");
	private String simplyBookCompanyLogin = System.getenv("SIMPLY_BOOK_COMPANY_LOGIN");
	private String simplyBookUser = System.getenv("SIMPLY_BOOK_USER_NAME");
	private String simplyBookPassword = new String(System.getenv("SIMPLY_BOOK_PASSWORD"));
	private String simplyBookApiKey = new String(System.getenv("SIMPLY_BOOK_API_KEY"));
	private String simplyBookSecretKey = new String(System.getenv("SIMPLY_BOOK_SECRET_KEY"));
	private String infusionSoftApiName = System.getenv("INFUSIONSOFT_API_NAME");
	private String infusionSoftApiKey = new String(System.getenv("INFUSIONSOFT_API_KEY"));
	private Integer infusionSoftAppliedNoShowTag = Integer.valueOf(System.getenv("INFUSIONSOFT_SBM_NO_SHOW_TAG"));
	private Integer infusionSoftAppliedCancelTag = Integer.valueOf(System.getenv("INFUSIONSOFT_SBM_CANCEL_TAG"));
	private Integer infusionSoftAppliedArrivedTag = Integer.valueOf(System.getenv("INFUSIONSOFT_SBM_ARRIVED_TAG"));
	private Integer statusClientNoShow = Integer.valueOf(System.getenv("STATUS_CLIENT_NO_SHOW"));
	private Integer statusClientArrived = Integer.valueOf(System.getenv("STATUS_CLIENT_ARRIVED"));

	private ShowBookingLambdaEnv() {
		assertVar(regions, "REGIONS");
		assertVar(regions, "REGIONS");
		assertVar(awsAccessKeyId, "AMAZON_ACCESS_KEY");
		assertVar(awsSecretAccessKey, "AMAZON_SECRET_ACCESS_KEY");
		assertVar(simplyBookServiceUrlLogin, "SIMPLY_BOOK_SERVICE_URL_lOGIN");
		assertVar(simplyBookAdminServiceUrl, "SIMPLY_BOOK_ADMIN_SERVICE_URL");
		assertVar(simplyBookCompanyLogin, "SIMPLY_BOOK_COMPANY_LOGIN");
		assertVar(simplyBookUser, "SIMPLY_BOOK_USER_NAME");
		assertVar(simplyBookPassword, "SIMPLY_BOOK_PASSWORD");
		assertVar(simplyBookApiKey, "SIMPLY_BOOK_API_KEY");
		assertVar(simplyBookSecretKey, "SIMPLY_BOOK_SECRET_KEY");
		assertVar(infusionSoftApiName, "INFUSIONSOFT_API_NAME");
		assertVar(infusionSoftApiKey, "INFUSIONSOFT_API_KEY");
		assertVar(infusionSoftAppliedNoShowTag, "INFUSIONSOFT_SBM_NO_SHOW_TAG");
		assertVar(infusionSoftAppliedCancelTag, "INFUSIONSOFT_SBM_CANCEL_TAG");
		assertVar(infusionSoftAppliedArrivedTag, "INFUSIONSOFT_SBM_ARRIVED_TAG");
		assertVar(statusClientNoShow, "STATUS_CLIENT_NO_SHOW");
		assertVar(statusClientArrived, "STATUS_CLIENT_ARRIVED");
	}

	private void assertVar(Object value, String name) {
		if (value == null) {
			throw new IllegalArgumentException("Enviroment variable " + name + " is not set");
		}
	}

	public synchronized static ShowBookingLambdaEnv load() {
		if (instance == null) {
			instance = new ShowBookingLambdaEnv();
		}
		return instance;
	}

	public Regions getRegions() {
		return regions;
	}

	public void setRegions(Regions regions) {
		this.regions = regions;
	}

	public String getAwsAccessKeyId() {
		return awsAccessKeyId;
	}

	public void setAwsAccessKeyId(String awsAccessKeyId) {
		this.awsAccessKeyId = awsAccessKeyId;
	}

	public String getAwsSecretAccessKey() {
		return awsSecretAccessKey;
	}

	public void setAwsSecretAccessKey(String awsSecretAccessKey) {
		this.awsSecretAccessKey = awsSecretAccessKey;
	}

	public String getSimplyBookServiceUrlLogin() {
		return simplyBookServiceUrlLogin;
	}

	public void setSimplyBookServiceUrlLogin(String simplyBookServiceUrlLogin) {
		this.simplyBookServiceUrlLogin = simplyBookServiceUrlLogin;
	}

	public String getSimplyBookAdminServiceUrl() {
		return simplyBookAdminServiceUrl;
	}

	public void setSimplyBookAdminServiceUrl(String simplyBookAdminServiceUrl) {
		this.simplyBookAdminServiceUrl = simplyBookAdminServiceUrl;
	}

	public String getSimplyBookCompanyLogin() {
		return simplyBookCompanyLogin;
	}

	public void setSimplyBookCompanyLogin(String simplyBookCompanyLogin) {
		this.simplyBookCompanyLogin = simplyBookCompanyLogin;
	}

	public String getSimplyBookUser() {
		return simplyBookUser;
	}

	public void setSimplyBookUser(String simplyBookUser) {
		this.simplyBookUser = simplyBookUser;
	}

	public String getSimplyBookPassword() {
		return simplyBookPassword;
	}

	public void setSimplyBookPassword(String simplyBookPassword) {
		this.simplyBookPassword = simplyBookPassword;
	}

	public String getSimplyBookApiKey() {
		return simplyBookApiKey;
	}

	public void setSimplyBookApiKey(String simplyBookApiKey) {
		this.simplyBookApiKey = simplyBookApiKey;
	}

	public String getSimplyBookSecretKey() {
		return simplyBookSecretKey;
	}

	public void setSimplyBookSecretKey(String simplyBookSecretKey) {
		this.simplyBookSecretKey = simplyBookSecretKey;
	}

	public static ShowBookingLambdaEnv getInstance() {
		return instance;
	}

	public String getInfusionSoftApiName() {
		return infusionSoftApiName;
	}

	public String getInfusionSoftApiKey() {
		return infusionSoftApiKey;
	}

	public Integer getInfusionSoftAppliedNoShowTag() {
		return infusionSoftAppliedNoShowTag;
	}

	public Integer getInfusionSoftAppliedCancelTag() {
		return infusionSoftAppliedCancelTag;
	}

	public Integer getInfusionSoftAppliedArrivedTag() {
		return infusionSoftAppliedArrivedTag;
	}

	public Integer getStatusClientNoShow() {
		return statusClientNoShow;
	}

	public Integer getStatusClientArrived() {
		return statusClientArrived;
	}

}
