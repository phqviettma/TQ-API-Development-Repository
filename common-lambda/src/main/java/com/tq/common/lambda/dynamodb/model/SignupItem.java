package com.tq.common.lambda.dynamodb.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;

@DynamoDBTable(tableName = "AffiliateSignup")
public class SignupItem implements Serializable {
    private static final long serialVersionUID = -39973517985217811L;
    
    @DynamoDBHashKey(attributeName = "email")
    private String email;
    
    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute(attributeName = "contact")
    private SignupInfo signup;
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public SignupInfo getSignup() {
        return signup;
    }
    public void setSignup(SignupInfo signup) {
        this.signup = signup;
    }
    
    public SignupItem withEmail(String email) {
        this.setEmail(email);
        return this;
    }

    public SignupItem withContactInfo(SignupInfo signup) {
        this.signup = signup;
        return this;
    }
    
}
