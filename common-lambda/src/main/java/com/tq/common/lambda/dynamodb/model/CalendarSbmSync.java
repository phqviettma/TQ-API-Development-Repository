package com.tq.common.lambda.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "CalendarSbmSync")
public class CalendarSbmSync {
	private String sbmId;
	private String email;
	private String lastname;
	private String firstname;
	@DynamoDBAttribute(attributeName = "sbmId")
	public String getSbmId() {
		return sbmId;
	}
	public void setSbmId(String sbmId) {
		this.sbmId = sbmId;
	}
	@DynamoDBHashKey(attributeName = "email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@DynamoDBAttribute(attributeName = "lastname")
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	@DynamoDBAttribute(attributeName = "firstname")
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	@Override
	public String toString() {
		return "CalendarSbmSync [sbmId=" + sbmId + ", email=" + email + ", lastname=" + lastname + ", firstname="
				+ firstname + "]";
	}
	
	
}
