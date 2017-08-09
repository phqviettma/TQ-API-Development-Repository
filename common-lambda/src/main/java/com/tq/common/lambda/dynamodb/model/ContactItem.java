package com.tq.common.lambda.dynamodb.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;

@DynamoDBTable(tableName = "Contact")
public class ContactItem implements Serializable{
    private static final long serialVersionUID = -6283601119856947731L;
    private String email;
    private ClientInfo client;

    @DynamoDBHashKey(attributeName = "email")
    public String getEmail() {
        return email;
    }

    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute(attributeName = "client")
    public ClientInfo getClient() {
        return client;
    }

    public void setClient(ClientInfo client) {
        this.client = client;
    }

    public ContactItem withEmail(String email) {
        this.setEmail(email);
        return this;
    }

    public ContactItem withContactInfo(ClientInfo clientInfo) {
        this.client = clientInfo;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ContactItem [email=" + email + ", client=" + client + "]";
    }
}
