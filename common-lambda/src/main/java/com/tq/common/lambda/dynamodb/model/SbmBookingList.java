package com.tq.common.lambda.dynamodb.model;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.tq.simplybook.resp.GetBookingResp;

@DynamoDBTable(tableName = "SbmBookingList")
public class SbmBookingList {
	private String sbmId;
	private List<GetBookingResp> bookingList;

	@DynamoDBHashKey(attributeName = "sbmId")
	public String getSbmId() {
		return sbmId;	
	}

	public void setSbmId(String sbmId) {
		this.sbmId = sbmId;
	}

	@DynamoDBAttribute(attributeName = "bookingList")
	@DynamoDBTyped(DynamoDBAttributeType.L)
	public List<GetBookingResp> getBookingList() {
		return bookingList;
	}

	public void setBookingList(List<GetBookingResp> bookingList) {
		this.bookingList = bookingList;
	}

	public SbmBookingList(String sbmId, List<GetBookingResp> bookingList) {

		this.sbmId = sbmId;
		this.bookingList = bookingList;
	}

	public SbmBookingList() {

	}

	@Override
	public String toString() {
		return "SbmBookingList [sbmId=" + sbmId + ", bookingList=" + bookingList + "]";
	}

}
