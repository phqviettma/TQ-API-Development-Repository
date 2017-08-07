package com.tq.clickfunnel.lambda.dynamodb.model;

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

    @DynamoDBHashKey(attributeName = "email")
    private String email;

    @DynamoDBAttribute(attributeName = "contactId")
    private Integer contactId;

    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute(attributeName = "recurringOrders")
    private List<RecurringOrder> recurringOrders;

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

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public OrderItem withRecurringOrders(List<RecurringOrder> recurringOrders) {
        this.recurringOrders = recurringOrders;
        return this;
    }

    public List<RecurringOrder> getRecurringOrders() {
        return recurringOrders;
    }

    public void setRecurringOrders(List<RecurringOrder> recurringOrders) {
        this.recurringOrders = recurringOrders;
    }

    public OrderItem withEmail(String email) {
        this.email = email;
        return this;
    }

    public OrderItem withContactId(Integer contactId) {
        this.contactId = contactId;
        return this;
    }

    @Override
    public String toString() {
        return "OrderItem [email=" + email + ", contactId=" + contactId + ", recurringOrders=" + recurringOrders + "]";
    }
}
