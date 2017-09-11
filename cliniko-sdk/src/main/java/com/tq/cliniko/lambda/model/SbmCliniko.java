package com.tq.cliniko.lambda.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
@DynamoDBTable(tableName="SbmClinikoSync")
public class SbmCliniko implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2369785511869975475L;
	private int sbmId;
	private int clinikoId;
	@DynamoDBHashKey(attributeName = "sbmId")
	public int getSbmId() {
		return sbmId;
	}

	public void setSbmId(int sbmId) {
		this.sbmId = sbmId;
	}
	@DynamoDBAttribute(attributeName = "clinikoId")
	public int getClinikoId() {
		return clinikoId;
	}

	public void setClinikoId(int clinikoId) {
		this.clinikoId = clinikoId;
	}

	@Override
	public String toString() {
		return "SbmCliniko [sbmId=" + sbmId + ", clinikoId=" + clinikoId + "]";
	}


	

}
