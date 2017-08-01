package com.tq.simplybook.lambda.context;

public class Env {

    private String m_awsAccessKeyId = System.getenv("AWS_ACCESS_KEY_ID");
    private String m_awsSecretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY");
    private String m_infusionSoftApiName = System.getenv("INFUSIONSOFT_API_NAME");
    private String m_infusionSoftApiKey = System.getenv("INFUSIONSOFT_API_KEY");
    private String m_infusionSoftCancelAppliedTag = System.getenv("INFUSIONSOFT_CANCEL_APPLIED_TAG");
    private String m_infusionSoftCustomFieldName = System.getenv("INFUSIONSOFT_CUSTOM_FILED_NAME");
    private String m_simplyBookServiceUrlLogin = System.getenv("SIMPLY_BOOK_SERVICE_URL_lOGIN");
    private String m_simplyBookAdminServiceUrl = System.getenv("SIMPLY_BOOK_ADMIN_SERVICE_URL");
    private String m_simplyBookCompanyLogin = System.getenv("SIMPLY_BOOK_COMPANY_LOGIN");
    private String m_simplyBookUser = System.getenv("SIMPLY_BOOK_USER");
    private String m_simplyBookPassword = System.getenv("SIMPLY_BOOK_PASSWORD");
    private String m_simplyBookApiKey = System.getenv("SIMPLY_BOOK_API_KEY");
    private String m_simplyBookSecretKey = System.getenv("SIMPLY_BOOK_SECRET_KEY");

    private static Env instance;

    private Env() {
        if (m_awsAccessKeyId == null || m_awsSecretAccessKey == null || m_infusionSoftApiName == null || m_infusionSoftApiKey == null 
                || m_infusionSoftCustomFieldName == null || m_simplyBookServiceUrlLogin == null || m_simplyBookAdminServiceUrl == null || m_simplyBookCompanyLogin == null || m_simplyBookUser == null
                || m_simplyBookPassword == null || m_simplyBookApiKey == null || m_simplyBookSecretKey == null) {
            throw new IllegalArgumentException("Enviroment variable is not set fully");
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

    public String getInfusionSoftCustomFieldName() {
        return m_infusionSoftCustomFieldName;
    }

    public void setInfusionSoftCustomFieldName(String infusionSoftCustomFieldName) {
        this.m_infusionSoftCustomFieldName = infusionSoftCustomFieldName;
    }
    
    public int getInfusionSoftCancelAppliedTag() {
        return Integer.valueOf(m_infusionSoftCancelAppliedTag);
    }

    public void setInfusionSoftCancelAppliedTag(String infusionSoftCancelAppliedTag) {
        this.m_infusionSoftCancelAppliedTag = infusionSoftCancelAppliedTag;
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
