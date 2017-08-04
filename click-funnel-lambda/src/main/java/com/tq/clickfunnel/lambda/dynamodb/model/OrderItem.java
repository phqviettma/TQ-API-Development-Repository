package com.tq.clickfunnel.lambda.dynamodb.model;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;
/**
 * 
 * @author phqviet
 * email field as hash key to detect with contact is adding order. 
 * Of course, contact can add lots of Orders
 */
@DynamoDBTable(tableName = "Order")
public class OrderItem {

    @DynamoDBHashKey(attributeName = "email")
    private String email;

    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute(attributeName = "infOrders")
    private List<OrderInf> infOrders;
    public OrderItem() {
    }

    public OrderItem(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<OrderInf> getInfOrders() {
        return infOrders;
    }

    public void setInfOrders(List<OrderInf> infOrders) {
        this.infOrders = infOrders;
    }
    
    public OrderItem withInfOrders(List<OrderInf> infOrders) {
        this.infOrders = infOrders;
        return this;
    }

    @Override
    public String toString() {
        return "OrderItem [email=" + email + ", infOrders=" + infOrders + "]";
    }
}
