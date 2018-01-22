package com.tq.simplybook.service;

import java.util.List;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.BookingInfo;

public interface BookingServiceSbm {
	BookingInfo getBookingInfo(String companyLogin, String endpoint, String token, Long id) throws SbmSDKException;

	boolean cancelBatch(String companyLogin, String endpoint, String token, String batchId, List<Long> bookingId) throws SbmSDKException;

	String createBatch(String companyLogin, String endpoint, String token) throws SbmSDKException;
	
}
