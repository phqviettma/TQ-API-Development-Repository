package com.tq.simplybook.lambda.context;

import com.amazonaws.regions.Regions;

public class Env {
    private Regions regions = Regions.valueOf(System.getenv("REGIONS"));

    private String awsAccessKeyId = new String(System.getenv("AWS_ACC_KEY_ID"));
    private String awsSecretAccessKey = new String(System.getenv("AWS_SEC_ACC_KEY"));
    private String infusionSoftApiName = System.getenv("IS_API_NAME");
    private String infusionSoftApiKey = new String(System.getenv("IS_API_KEY"));
    private Integer infusionSoftCancelAppliedTag = Integer.valueOf(System.getenv("IS_CANCEL_TAG"));
    private Integer infusionSoftCreateAppliedTag = Integer.valueOf(System.getenv("IS_CREATE_TAG"));
    
    private String infusionSoftAppointmentTimeField = System.getenv("IS_APT_TIME_FILED");
    private String infusionSoftAppointmentLocationField = System.getenv("IS_APT_LOC_FIELD");
    private String infusionSoftServiceProviderField = System.getenv("IS_SER_PROV_FIELD");
    private String infusionSoftAppointmentInstructionField = System.getenv("IS_APT_INST_FIELD");
    
    private String simplyBookServiceUrlLogin = System.getenv("SBM_SRV_URL_LOGIN");
    private String simplyBookAdminServiceUrl = System.getenv("SBM_ADM_SRV_URL");
    private String simplyBookCompanyLogin = System.getenv("SBM_COMP_LOGIN");
    private String simplyBookUser = System.getenv("SBM_USER");
    private String simplyBookPassword = new String(System.getenv("SBM_PASSWORD"));
    private String simplyBookApiKey = new String(System.getenv("SBM_KEY"));
    private String simplyBookSecretKey = new String(System.getenv("SBM_SECRET_KEY"));

    private Env() {
        assertVar(regions, "REGIONS");
        assertVar(awsAccessKeyId, "AWS_ACC_KEY_ID");
        assertVar(awsSecretAccessKey, "AWS_SEC_ACC_KEY");
        assertVar(infusionSoftApiName, "IS_API_NAME");
        assertVar(infusionSoftApiKey, "IS_API_KEY");
        assertVar(infusionSoftCancelAppliedTag, "IS_CANCEL_TAG");
        assertVar(infusionSoftCreateAppliedTag, "IS_CREATE_TAG");
        assertVar(infusionSoftAppointmentTimeField, "IS_APT_TIME_FILED");
        assertVar(infusionSoftAppointmentLocationField, "IS_APT_LOC_FIELD");
        assertVar(infusionSoftServiceProviderField, "IS_SER_PROV_FIELD");
        assertVar(infusionSoftAppointmentInstructionField, "IS_APT_INST_FIELD");
        assertVar(simplyBookServiceUrlLogin, "SBM_SRV_URL_LOGIN");
        assertVar(simplyBookAdminServiceUrl, "SBM_ADM_SRV_URL");
        assertVar(simplyBookCompanyLogin, "SBM_COMP_LOGIN");
        assertVar(simplyBookUser, "SBM_USER");
        assertVar(simplyBookPassword, "SBM_PASSWORD");
        assertVar(simplyBookApiKey, "SBM_KEY");
        assertVar(simplyBookSecretKey, "SBM_SECRET_KEY");
    }
    
    private static Env instance;
    
    public static Env getInstance() {
        return instance;
    }

    
    private void assertVar(Object value, String name){
        if (value == null){
            throw new IllegalArgumentException("Enviroment variable " + name + " is not set");
        }
    }
        

    public synchronized static Env load() {
        if (instance == null) {
            instance = new Env();
        }
        return instance;
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

    public String getInfusionSoftApiName() {
        return infusionSoftApiName;
    }

    public String getInfusionSoftApiKey() {
        return infusionSoftApiKey;
    }

    public Integer getInfusionSoftCancelAppliedTag() {
        return Integer.valueOf(infusionSoftCancelAppliedTag);
    }

    public Integer getInfusionSoftCreateAppliedTag() {
        return Integer.valueOf(infusionSoftCreateAppliedTag);
    }

    public String getInfusionSoftAppointmentTimeField() {
        return infusionSoftAppointmentTimeField;
    }

    public String getInfusionSoftAppointmentLocationField() {
        return infusionSoftAppointmentLocationField;
    }

    public String getInfusionSoftServiceProviderField() {
        return infusionSoftServiceProviderField;
    }

    public String getInfusionSoftAppointmentInstructionField() {
        return infusionSoftAppointmentInstructionField;
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
