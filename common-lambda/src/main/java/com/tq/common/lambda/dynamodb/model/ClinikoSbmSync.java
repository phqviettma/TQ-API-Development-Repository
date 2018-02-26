package com.tq.common.lambda.dynamodb.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ClinikoSyncToSbm")
public class ClinikoSbmSync implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8757196488493509750L;
	private String apiKey;
	private String email;
	private String clinikoId;
	private Integer unitId;
	private Integer serviceId;

	@DynamoDBHashKey(attributeName = "apiKey")
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "Email-Index", attributeName = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@DynamoDBAttribute(attributeName = "unitId")
	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	@DynamoDBAttribute(attributeName = "serviceId")
	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public ClinikoSbmSync(String apiKey, String email, String clinikoId, Integer unitId, Integer serviceId) {

		this.apiKey = apiKey;
		this.email = email;
		this.clinikoId = clinikoId;
		this.unitId = unitId;
		this.serviceId = serviceId;
	}

	@Override
	public String toString() {
		return "ClinikoSbmSync [apiKey=" + apiKey + ", email=" + email + ", clinikoId=" + clinikoId + ", unitId="
				+ unitId + ", serviceId=" + serviceId + "]";
	}
	@DynamoDBAttribute(attributeName = "clinikoId")
	public String getClinikoId() {
		return clinikoId;
	}

	public void setClinikoId(String clinikoId) {
		this.clinikoId = clinikoId;
	}

	public ClinikoSbmSync() {

	}

}
