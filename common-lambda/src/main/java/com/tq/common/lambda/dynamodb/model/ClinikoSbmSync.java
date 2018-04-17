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
	private String sbmId;

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "ClinikoSbm-Index", attributeName = "apiKey")
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@DynamoDBHashKey(attributeName = "sbmId")
	public String getSbmId() {
		return sbmId;
	}

	public void setSbmId(String sbmId) {
		this.sbmId = sbmId;
	}

	@DynamoDBAttribute(attributeName = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ClinikoSbmSync(String apiKey, String email, String clinikoId, String sbmId) {
		this.apiKey = apiKey;
		this.email = email;
		this.clinikoId = clinikoId;
		this.sbmId = sbmId;
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

	@Override
	public String toString() {
		return "ClinikoSbmSync [apiKey=" + apiKey + ", email=" + email + ", clinikoId=" + clinikoId + ", sbmId=" + sbmId
				+ "]";
	}

}
