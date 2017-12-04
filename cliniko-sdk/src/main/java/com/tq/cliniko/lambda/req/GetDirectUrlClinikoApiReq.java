package com.tq.cliniko.lambda.req;

public class GetDirectUrlClinikoApiReq extends DirectUrlClinikoApiReq {

	public GetDirectUrlClinikoApiReq(String apiKey, String url) {
		super(apiKey, "GET", url);
	}

}
