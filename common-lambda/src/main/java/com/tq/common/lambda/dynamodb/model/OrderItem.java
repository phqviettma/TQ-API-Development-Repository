package com.tq.common.lambda.dynamodb.model;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;

/**
 * 
 * @author phqviet email field as hash key to detect with contact is adding order. Of course, contact can add lots of Orders
 */
@DynamoDBTable(tableName = "Order")
public class OrderItem {
    
    @DynamoDBHashKey(attributeName = "purchaseId")
    private Integer purchaseId;

    @DynamoDBAttribute(attributeName = "email")
    private String email;
    
    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute(attributeName = "orderDetails")
    private List<OrderDetail> orderDetails; 

    public OrderItem() {
    }
    
    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }
    
    public OrderItem withPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
        return this;
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

    public OrderItem withEmail(String email) {
        this.email = email;
        return this;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
    
    public OrderItem withOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
        return this;
    }

    @Override
    public String toString() {
        return "OrderItem [purchaseId=" + purchaseId + ", email=" + email + ", orderDetails=" + orderDetails + "]";
    }
}
