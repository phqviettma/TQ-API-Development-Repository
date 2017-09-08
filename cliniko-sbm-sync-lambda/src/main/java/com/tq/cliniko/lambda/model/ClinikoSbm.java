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
	private String clinikoUnavailableTime;
	private String sbmUnavailableTime;

	@DynamoDBHashKey(attributeName = "clinikoId")
	public int getClinikoId() {
		return clinikoId;
	}

	public void setClinikoId(int clinikoId) {
		this.clinikoId = clinikoId;
	}

	@DynamoDBAttribute(attributeName = "clinikoUnvailableTime")
	public String getClinikoUnavailableTime() {
		return clinikoUnavailableTime;
	}

	public void setClinikoUnavailableTime(String clinikoUnavailableTime) {
		this.clinikoUnavailableTime = clinikoUnavailableTime;
	}

	@DynamoDBAttribute(attributeName = "sbmUnavailableTime")
	public String getSbmUnavailableTime() {
		return sbmUnavailableTime;
	}

	public void setSbmUnavailableTime(String sbmUnavailableTime) {
		this.sbmUnavailableTime = sbmUnavailableTime;
	}

	@Override
	public String toString() {
		return "ClinikoSbm [clinikoId=" + clinikoId + ", clinikoUnavailableTime=" + clinikoUnavailableTime
				+ ", sbmUnavailableTime=" + sbmUnavailableTime + "]";
	}

}
