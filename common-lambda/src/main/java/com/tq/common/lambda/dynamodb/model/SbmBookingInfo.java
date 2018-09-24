package com.tq.common.lambda.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "SbmBookingInfo")
public class SbmBookingInfo {
	private Long bookingId;
	private String email;
	private String unitName;
	private String unitAddress;
	private String apptTime;
	private Long timeStamp;
	private String clientName;
	private String clientPhone;
	private String clientEmail;
	private String apptDate;
	private String bookingStatus;
	private String unitDescription;

	@DynamoDBHashKey(attributeName = "bookingId")
	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	@DynamoDBIndexHashKey(attributeName = "email", globalSecondaryIndexName = "practitioner-index")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@DynamoDBAttribute(attributeName = "unitName")
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@DynamoDBAttribute(attributeName = "clientName")
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@DynamoDBAttribute(attributeName = "clientPhone")
	public String getClientPhone() {
		return clientPhone;
	}

	public void setClientPhone(String clientPhone) {
		this.clientPhone = clientPhone;
	}

	@DynamoDBIndexHashKey(attributeName = "clientEmail", globalSecondaryIndexName = "Client-Index")
	public String getClientEmail() {
		return clientEmail;
	}

	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}

	@DynamoDBAttribute(attributeName = "apptTime")
	public String getApptTime() {
		return apptTime;
	}

	@DynamoDBAttribute(attributeName = "unitAddress")
	public String getUnitAddress() {
		return unitAddress;
	}

	public void setUnitAddress(String unitAddress) {
		this.unitAddress = unitAddress;
	}

	public void setApptTime(String apptTime) {
		this.apptTime = apptTime;
	}

	@DynamoDBAttribute(attributeName = "apptDate")
	public String getApptDate() {
		return apptDate;
	}

	public void setApptDate(String apptDate) {
		this.apptDate = apptDate;
	}

	@DynamoDBAttribute(attributeName="timeStamp")
	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@DynamoDBAttribute(attributeName = "bookingStatus")
	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	
	
	@DynamoDBAttribute(attributeName ="unitDescription")
	public String getUnitDescription() {
		return unitDescription;
	}

	public void setUnitDescription(String unitDescription) {
		this.unitDescription = unitDescription;
	}

	public SbmBookingInfo(Long bookingId, String email, String unitName, String unitAddress, String apptTime,
			Long timeStamp, String clientName, String clientPhone, String clientEmail, String apptDate, String unitDescription) {
		this.bookingId = bookingId;
		this.email = email;
		this.unitName = unitName;
		this.unitAddress = unitAddress;
		this.apptTime = apptTime;
		this.timeStamp = timeStamp;
		this.clientName = clientName;
		this.clientPhone = clientPhone;
		this.clientEmail = clientEmail;
		this.apptDate = apptDate;
		this.unitDescription = unitDescription;
	}

	public SbmBookingInfo(Long bookingId, String email, String clientName, String clientEmail, String apptTime,
			String apptDate, Long timeStamp, String unitName) {
		this.bookingId = bookingId;
		this.email = email;
		this.clientName = clientName;
		this.clientEmail = clientEmail;
		this.apptTime = apptTime;
		this.apptDate = apptDate;
		this.timeStamp = timeStamp;
		this.unitName = unitName;
	}

	public SbmBookingInfo() {

	}

	@Override
	public String toString() {
		return "SbmBookingInfo [bookingId=" + bookingId + ", email=" + email + ", unitName=" + unitName
				+ ", unitAddress=" + unitAddress + ", apptTime=" + apptTime + ", timeStamp=" + timeStamp
				+ ", clientName=" + clientName + ", clientPhone=" + clientPhone + ", clientEmail=" + clientEmail
				+ ", apptDate=" + apptDate + "]";
	}

}
