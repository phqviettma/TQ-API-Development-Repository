package com.tq.common.lambda.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Country")
public class CountryItem {
    
    @DynamoDBHashKey(attributeName = "name")
    private String name;
    
    @DynamoDBIndexHashKey(globalSecondaryIndexName="Country-Index",attributeName = "code")
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
