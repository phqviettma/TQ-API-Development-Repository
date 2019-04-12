package com.tq.common.lambda.dynamodb.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ClinikoCompanyInfo")
public class ClinikoCompanyInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2885786689109867984L;
	private Integer businessId;
	private Integer appointmentType;
	private String apiKey;

	@DynamoDBAttribute(attributeName = "appointmentTypeId")
	public Integer getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(Integer appointmentType) {
		this.appointmentType = appointmentType;
	}

	public ClinikoCompanyInfo() {

	}

	@DynamoDBAttribute(attributeName = "businessId")
	public Integer getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}

	public ClinikoCompanyInfo(Integer businessId, Integer appointmentType, String apiKey) {

		this.businessId = businessId;
		this.appointmentType = appointmentType;
		this.apiKey = apiKey;
	}

	@DynamoDBHashKey(attributeName = "apiKey")
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public String toString() {
		return "ClinikoCompanyInfo [businessId=" + businessId + ", appointmentType=" + appointmentType + ", apiKey="
				+ apiKey + "]";
	}

}
