package com.tq.cliniko.lambda.req;

public class GetClinikoApiReq  extends GenericClinikoApiReq{

	public GetClinikoApiReq(String apiKey, String resource) {
		super(apiKey, "GET", resource, null, null);
	
	}

}
