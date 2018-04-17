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
	private Integer patientId;
	private Integer businessId;
	private Integer appointmentType;

	@DynamoDBAttribute(attributeName = "patientId")
	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	@DynamoDBAttribute(attributeName = "appointmentTypeId")
	public Integer getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(Integer appointmentType) {
		this.appointmentType = appointmentType;
	}

	public ClinikoCompanyInfo() {

	}

	@DynamoDBHashKey(attributeName = "businessId")
	public Integer getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}

	public ClinikoCompanyInfo(Integer patientId, Integer businessId, Integer appointmentType) {

		this.patientId = patientId;
		this.businessId = businessId;
		this.appointmentType = appointmentType;
	}

	@Override
	public String toString() {
		return "ClinikoCompanyInfo [patientId=" + patientId + ", businessId=" + businessId
				+ ", appointmentType=" + appointmentType + "]";
	}

}
