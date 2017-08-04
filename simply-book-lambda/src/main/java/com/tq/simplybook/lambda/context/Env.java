package com.tq.simplybook.lambda.context;

import com.tq.simplybook.lambda.util.Util;

public class Env {

    private String m_awsAccessKeyId = Util.decryptAwsKey(System.getenv("AWS_ACCESS_KEY_ID_TQ"));
    private String m_awsSecretAccessKey = Util.decryptAwsKey(System.getenv("AWS_SECRET_ACCESS_KEY_TQ"));
    private String m_infusionSoftApiName = System.getenv("INFUSIONSOFT_API_NAME");
    private String m_infusionSoftApiKey = Util.decryptAwsKey(System.getenv("INFUSIONSOFT_API_KEY"));
    private String m_infusionSoftCancelAppliedTag = System.getenv("INFUSIONSOFT_CANCEL_APPLIED_TAG");
    private String m_infusionSoftCreateAppliedTag = System.getenv("INFUSIONSOFT_CREATE_APPLIED_TAG");
    
    private String m_infusionSoftAppointmentTimeField = System.getenv("INFUSIONSOFT_APPOINTMENT_TIME_FIELD");
    private String m_infusionSoftAppointmentLocationField = System.getenv("INFUSIONSOFT_APPOINTMENT_LOCATION_FIELD");
    private String m_infusionSoftServiceProviderField = System.getenv("INFUSIONSOFT_SERVICE_PROVIDER_FIELD");
    private String m_infusionSoftAppointmentInstructionField = System.getenv("INFUSIONSOFT_APPOINTMENT_INSTRUCTION_FIELD");
    
    private String m_simplyBookServiceUrlLogin = System.getenv("SIMPLY_BOOK_SERVICE_URL_lOGIN");
    private String m_simplyBookAdminServiceUrl = System.getenv("SIMPLY_BOOK_ADMIN_SERVICE_URL");
    private String m_simplyBookCompanyLogin = System.getenv("SIMPLY_BOOK_COMPANY_LOGIN");
    private String m_simplyBookUser = System.getenv("SIMPLY_BOOK_USER");
    private String m_simplyBookPassword = Util.decryptAwsKey(System.getenv("SIMPLY_BOOK_PASSWORD"));
    private String m_simplyBookApiKey = Util.decryptAwsKey(System.getenv("SIMPLY_BOOK_API_KEY"));
    private String m_simplyBookSecretKey = Util.decryptAwsKey(System.getenv("SIMPLY_BOOK_SECRET_KEY"));

    private static Env instance;

    private Env() {
        assertVar(m_awsAccessKeyId, "AWS_ACCESS_KEY_ID_TQ");
        assertVar(m_awsSecretAccessKey, "AWS_SECRET_ACCESS_KEY_TQ");
        assertVar(m_infusionSoftApiName, "INFUSIONSOFT_API_NAME");
        assertVar(m_infusionSoftApiKey, "INFUSIONSOFT_API_KEY");
        assertVar(m_infusionSoftCancelAppliedTag, "INFUSIONSOFT_CANCEL_APPLIED_TAG");
        assertVar(m_infusionSoftCreateAppliedTag, "INFUSIONSOFT_CREATE_APPLIED_TAG");
        assertVar(m_infusionSoftAppointmentTimeField, "INFUSIONSOFT_APPOINTMENT_TIME_FIELD");
        assertVar(m_infusionSoftAppointmentLocationField, "INFUSIONSOFT_APPOINTMENT_LOCATION_FIELD");
        assertVar(m_infusionSoftServiceProviderField, "INFUSIONSOFT_SERVICE_PROVIDER_FIELD");
        assertVar(m_infusionSoftAppointmentInstructionField, "INFUSIONSOFT_APPOINTMENT_INSTRUCTION_FIELD");
        assertVar(m_simplyBookServiceUrlLogin, "SIMPLY_BOOK_SERVICE_URL_lOGIN");
        assertVar(m_simplyBookAdminServiceUrl, "SIMPLY_BOOK_ADMIN_SERVICE_URL");
        assertVar(m_simplyBookCompanyLogin, "SIMPLY_BOOK_COMPANY_LOGIN");
        assertVar(m_simplyBookUser, "SIMPLY_BOOK_USER");
        assertVar(m_simplyBookPassword, "SIMPLY_BOOK_PASSWORD");
        assertVar(m_simplyBookApiKey, "SIMPLY_BOOK_API_KEY");
        assertVar(m_simplyBookSecretKey, "SIMPLY_BOOK_SECRET_KEY");
    }

    private void assertVar(String value, String name){
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

    public String getAwsAccessKeyId() {
        return m_awsAccessKeyId;
    }

    public void setAwsAccessKeyId(String awsAccessKeyId) {
        this.m_awsAccessKeyId = awsAccessKeyId;
    }

    public String getAwsSecretAccessKey() {
        return m_awsSecretAccessKey;
    }

    public void setAwsSecretAccessKey(String awsSecretAccessKey) {
        this.m_awsSecretAccessKey = awsSecretAccessKey;
    }

    public String getInfusionSoftApiName() {
        return m_infusionSoftApiName;
    }

    public void setInfusionSoftApiName(String infusionSoftApiName) {
        this.m_infusionSoftApiName = infusionSoftApiName;
    }

    public String getInfusionSoftApiKey() {
        return m_infusionSoftApiKey;
    }

    public void setInfusionSoftApiKey(String infusionSoftApiKey) {
        this.m_infusionSoftApiKey = infusionSoftApiKey;
    }
    
    public int getInfusionSoftCancelAppliedTag() {
        return Integer.valueOf(m_infusionSoftCancelAppliedTag);
    }

    public void setInfusionSoftCancelAppliedTag(String infusionSoftCancelAppliedTag) {
        this.m_infusionSoftCancelAppliedTag = infusionSoftCancelAppliedTag;
    }

    
    public int getInfusionSoftCreateAppliedTag() {
        return Integer.valueOf(m_infusionSoftCreateAppliedTag);
    }

    public void setInfusionSoftCreateAppliedTag(String infusionSoftCreateAppliedTag) {
        this.m_infusionSoftCreateAppliedTag = infusionSoftCreateAppliedTag;
    }

    public String getInfusionSoftAppointmentTimeField() {
        return m_infusionSoftAppointmentTimeField;
    }

    public void setInfusionSoftAppointmentTimeField(String infusionSoftAppointmentTimeField) {
        this.m_infusionSoftAppointmentTimeField = infusionSoftAppointmentTimeField;
    }

    public String getInfusionSoftAppointmentLocationField() {
        return m_infusionSoftAppointmentLocationField;
    }

    public void setInfusionSoftAppointmentLocationField(String infusionSoftAppointmentLocationField) {
        this.m_infusionSoftAppointmentLocationField = infusionSoftAppointmentLocationField;
    }

    public String getInfusionSoftServiceProviderField() {
        return m_infusionSoftServiceProviderField;
    }

    public void setInfusionSoftServiceProviderField(String infusionSoftServiceProviderField) {
        this.m_infusionSoftServiceProviderField = infusionSoftServiceProviderField;
    }

    public String getInfusionSoftAppointmentInstructionField() {
        return m_infusionSoftAppointmentInstructionField;
    }

    public void setInfusionSoftAppointmentInstructionField(String infusionSoftAppointmentInstructionField) {
        this.m_infusionSoftAppointmentInstructionField = infusionSoftAppointmentInstructionField;
    }
    
    public String getSimplyBookServiceUrlLogin() {
        return m_simplyBookServiceUrlLogin;
    }

    public String getSimplyBookAdminServiceUrl() {
        return m_simplyBookAdminServiceUrl;
    }

    public String getSimplyBookCompanyLogin() {
        return m_simplyBookCompanyLogin;
    }

    public String getSimplyBookUser() {
        return m_simplyBookUser;
    }

    public String getSimplyBookPassword() {
        return m_simplyBookPassword;
    }

    public String getSimplyBookApiKey() {
        return m_simplyBookApiKey;
    }

    public String getSimplyBookSecretKey() {
        return m_simplyBookSecretKey;
    }
    
}
