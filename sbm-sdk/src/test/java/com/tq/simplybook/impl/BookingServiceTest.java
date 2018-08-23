package com.tq.simplybook.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.GetBookingReq;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.resp.GetBookingResp;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class BookingServiceTest {
	private static TokenServiceSbm tokenService = null;
	private static BookingServiceSbm bookingService = null;

	@Before
	public void init() {
		tokenService = new TokenServiceImpl();
		bookingService = new BookingServiceSbmImpl();
	}

	@Test
	public void testGetBookingDetail() throws SbmSDKException {
		String companyLogin = "truequit";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String token = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		BookingInfo info = bookingService.getBookingInfo(companyLogin, endpoint, token, 259L);
		assertNotNull(info.getClient_email());
	}

	@Test
	@Ignore
	public void testGetListBooking() throws SbmSDKException {
		String companyLogin = "canhcanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String token = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		GetBookingReq e = new GetBookingReq("2018-04-25", "non_cancelled", "start_date", 2, 1);
		List<GetBookingResp> bookings = bookingService.getBookings(companyLogin, endpoint, token, e);
		assertTrue(bookings.size() > 0);

	}

	@Test
	@Ignore
	public void testCancelBatch() throws SbmSDKException {
		String companyLogin = "truequit";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		List<Long> bookingId = Arrays.asList(142L);
		boolean cancelled = bookingService.cancelBatch(companyLogin, endpoint, userToken, "10", bookingId);
		assertTrue(cancelled);
	}

	@Test
	@Ignore
	public void testBooking() throws Exception {
		String companyLogin = "canhcanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String token = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		GetBookingReq getBookingReq = new GetBookingReq("date_from", "booking_type", "order", 1, 1);
		List<GetBookingResp> booking = bookingService.getBookings(companyLogin, endpoint, token, getBookingReq);
		assertNotNull(booking);
	}

}
