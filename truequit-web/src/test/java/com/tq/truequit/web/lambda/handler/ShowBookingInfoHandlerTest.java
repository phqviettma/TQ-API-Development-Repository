package com.tq.truequit.web.lambda.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.tq.common.lambda.dynamodb.model.SbmBookingInfo;
import com.tq.common.lambda.dynamodb.service.SbmBookingInfoService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.truequit.web.lambda.request.RequestParams;

public class ShowBookingInfoHandlerTest {
	private SbmBookingInfoService sbmBookingInfoService = mock(SbmBookingInfoService.class);
	ShowBookingInfoHandler showHandler = new ShowBookingInfoHandler(sbmBookingInfoService);

	@Test
	public void testLoad() throws InfSDKExecption {
		
		ShowBookingRequest request = new ShowBookingRequest();
		RequestParams params = new RequestParams();
		params.setEmail("pttsuong@tma.com.vn");
		params.setSize(10);
		request.setParams(params );
		QueryResultPage<SbmBookingInfo> result = new QueryResultPage<>();
		result.setCount(10);
		List<SbmBookingInfo> bookings = new ArrayList<>();
		SbmBookingInfo bookingInfo = new SbmBookingInfo();
		bookings.add(bookingInfo );
		result.setResults(bookings );
		when(sbmBookingInfoService.getListBooking(request.getParams().getEmail(), request.getParams().getSize())).thenReturn(result );
	    showHandler.handle(request);
	}
}
