package com.tq.simplybook.service;

import java.util.List;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.GetBookingReq;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.resp.GetBookingResp;

public interface BookingServiceSbm {
	BookingInfo getBookingInfo(String companyLogin, String endpoint, String token, Long id) throws SbmSDKException;

	boolean cancelBatch(String companyLogin, String endpoint, String token, String batchId, List<Long> bookingId) throws SbmSDKException;

	String createBatch(String companyLogin, String endpoint, String token) throws SbmSDKException;
	
	List<GetBookingResp> getBookings(String companyLogin, String endpoint, String token,
			GetBookingReq getBookingReq) throws SbmSDKException;
}
