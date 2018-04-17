package com.tq.common.lambda.dynamodb.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "SbmClinikoSync")
public class SbmCliniko implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2369785511869975475L;
	private Long sbmId;
	private Long clinikoId;
	private Integer flag;
	private String apiKey;
	private String agent;

	@DynamoDBHashKey(attributeName = "sbmId")
	public Long getSbmId() {
		return sbmId;
	}

	public void setSbmId(Long sbmId) {
		this.sbmId = sbmId;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "SbmCliniko-Index", attributeName = "clinikoId")
	public Long getClinikoId() {
		return clinikoId;
	}

	public void setClinikoId(Long clinikoId) {
		this.clinikoId = clinikoId;
	}

	public SbmCliniko withClinikoId(Long clinikoId) {
		this.clinikoId = clinikoId;
		return this;
	}

	public SbmCliniko withSbmId(Long sbmId) {
		this.sbmId = sbmId;
		return this;
	}

	@DynamoDBAttribute(attributeName = "agent")
	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	@DynamoDBAttribute(attributeName = "flag")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@DynamoDBAttribute(attributeName = "apiKey")
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public String toString() {
		return "SbmCliniko [sbmId=" + sbmId + ", clinikoId=" + clinikoId + ", flag=" + flag + ", apiKey=" + apiKey
				+ ", agent=" + agent + "]";
	}

	public SbmCliniko(Long sbmId, Long clinikoId, Integer flag, String apiKey, String agent) {

		this.sbmId = sbmId;
		this.clinikoId = clinikoId;
		this.flag = flag;
		this.apiKey = apiKey;
		this.agent = agent;
	}

	public SbmCliniko() {

	}

}
