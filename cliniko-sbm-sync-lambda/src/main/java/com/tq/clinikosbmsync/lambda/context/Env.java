package com.tq.clinikosbmsync.lambda.context;

import com.amazonaws.regions.Regions;

public class Env {
	private static Env instance;
	
	private final String clinikoApiKey = new String(System.getenv("CLINIKO_API_KEY"));
	private final String timezone = new String(System.getenv("TQ_TIME_ZONE"));
	
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

    private Env() {
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
    }
    
    private void assertVar(Object value, String name){
        if (value == null){
            throw new IllegalArgumentException("Enviroment variable " + name + " is not set");
        }
    }
	
	public String getTimezone() {
		return timezone;
	}

	public synchronized static Env load() {
        if (instance == null) {
            instance = new Env();
        }
        return instance;
    }
	
	public String getClinikoApiKey() {
		return clinikoApiKey;
	}
	
	public Regions getRegions() {
        return regions;
    }

    public String getAwsAccessKeyId() {
        return awsAccessKeyId;
    }

    public String getAwsSecretAccessKey() {
        return awsSecretAccessKey;
    }

    public String getSimplyBookServiceUrlLogin() {
        return simplyBookServiceUrlLogin;
    }

    public String getSimplyBookAdminServiceUrl() {
        return simplyBookAdminServiceUrl;
    }

    public String getSimplyBookCompanyLogin() {
        return simplyBookCompanyLogin;
    }

    public String getSimplyBookUser() {
        return simplyBookUser;
    }

    public String getSimplyBookPassword() {
        return simplyBookPassword;
    }

    public String getSimplyBookApiKey() {
        return simplyBookApiKey;
    }

    public String getSimplyBookSecretKey() {
        return simplyBookSecretKey;
    }
}
