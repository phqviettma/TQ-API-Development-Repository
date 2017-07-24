package com.tq.simplybook.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTokenReq implements Serializable{
    private static final long serialVersionUID = -2591842122111164628L;
    private String companyLogin;
    private String userLogin;
    private String userPassword;
    
    public UserTokenReq() {
    }
    
    public UserTokenReq(String companyLogin, String userLogin, String userPassword) {
        super();
        this.companyLogin = companyLogin;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }

    public String getCompanyLogin() {
        return companyLogin;
    }
    public void setCompanyLogin(String companyLogin) {
        this.companyLogin = companyLogin;
    }
    public String getUserLogin() {
        return userLogin;
    }
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    @Override
    public String toString() {
        return "UserTokenReq [companyLogin=" + companyLogin + ", userLogin=" + userLogin + ", userPassword=" + userPassword + "]";
    }
}
