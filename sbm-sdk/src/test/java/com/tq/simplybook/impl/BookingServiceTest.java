package com.tq.simplybook.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.BookingInfo;

public class BookingServiceTest {
	private static TokenServiceImpl tokenService = new TokenServiceImpl();
	private static BookingServiceSbmImpl bookingService = new BookingServiceSbmImpl();

	@Test
	public void testGetBookingDetail() throws SbmSDKException {
		String companyLogin = "trancanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		BookingInfo info = bookingService.getBookingInfo(companyLogin, endpoint, userToken, 5L);
		assertNotNull(info);
	}

}
