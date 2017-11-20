package com.tq.simplybook.service;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.BookingInfo;

public interface BookingServiceSbm {
	BookingInfo getBookingInfo(String companyLogin, String endpoint, String token, Long id) throws SbmSDKException;
	
}
