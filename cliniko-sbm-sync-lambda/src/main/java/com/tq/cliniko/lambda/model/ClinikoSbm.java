package com.tq.cliniko.lambda.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ClinikoSbmSync")
public class ClinikoSbm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6828809468118857668L;
	private int clinikoId;
	private String clinikoBlockTime;
	private String sbmBlockTime;

	@DynamoDBHashKey(attributeName = "clinikoId")
	public int getClinikoId() {
		return clinikoId;
	}

	public void setClinikoId(int clinikoId) {
		this.clinikoId = clinikoId;
	}

	@DynamoDBAttribute(attributeName = "clinikoBlockTime")
	public String getclinikoBlockTime() {
		return clinikoBlockTime;
	}

	public void setclinikoBlockTime(String clinikoBlockTime) {
		this.clinikoBlockTime = clinikoBlockTime;
	}

	@DynamoDBAttribute(attributeName = "sbmBlockTime")
	public String getsbmBlockTime() {
		return sbmBlockTime;
	}

	public void setsbmBlockTime(String sbmBlockTime) {
		this.sbmBlockTime = sbmBlockTime;
	}

	@Override
	public String toString() {
		return "ClinikoSbm [clinikoId=" + clinikoId + ", clinikoBlockTime=" + clinikoBlockTime
				+ ", sbmBlockTime=" + sbmBlockTime + "]";
	}

}
