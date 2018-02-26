package com.tq.simplybook.impl;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.GetBookingReq;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.resp.GetBookingResp;

public class BookingServiceTest {
	private static TokenServiceImpl tokenService = new TokenServiceImpl();
	private static BookingServiceSbmImpl bookingService = new BookingServiceSbmImpl();

	@Test
	public void testGetBookingDetail() throws SbmSDKException {
		String companyLogin = "canhcanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "1900561594";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		BookingInfo info = bookingService.getBookingInfo(companyLogin, endpoint, userToken, 5L);
		assertNotNull(info);
	}

	@Test
	public void testGetListBooking() throws SbmSDKException {
		String companyLogin = "canhcanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "1900561594";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		try {
			GetBookingReq e = new GetBookingReq("2018-01-01", "2018-01-30", "all", "start_date", 6, 1);
			List<GetBookingResp> bookingList = bookingService.getBookings(companyLogin, endpoint, userToken, e);
			assertNotNull(bookingList);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
