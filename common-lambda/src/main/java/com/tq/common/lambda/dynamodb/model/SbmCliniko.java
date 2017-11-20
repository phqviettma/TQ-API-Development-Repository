package com.tq.common.lambda.dynamodb.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;

@DynamoDBTable(tableName = "SbmClinikoSync")
public class SbmCliniko implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2369785511869975475L;
	private Long sbmId;
	private Long clinikoId;

	@DynamoDBHashKey(attributeName = "sbmId")
	public Long getSbmId() {
		return sbmId;
	}

	public void setSbmId(Long sbmId) {
		this.sbmId = sbmId;
	}
	@DynamoDBTypeConvertedJson
	@DynamoDBAttribute(attributeName = "clinikoId")
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

	@Override
	public String toString() {
		return "SbmCliniko [sbmId=" + sbmId + ", clinikoId=" + clinikoId + "]";
	}

}
