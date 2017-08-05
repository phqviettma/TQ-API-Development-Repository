package com.tq.clickfunnel.lambda.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Country")
public class CountryItem {
    
    @DynamoDBHashKey(attributeName = "name")
    private String name;
    
    @DynamoDBAttribute(attributeName = "code")
    private String code;

    public CountryItem() {
    }
    
    public CountryItem(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "CountryItem [name=" + name + ", code=" + code + "]";
    }
}