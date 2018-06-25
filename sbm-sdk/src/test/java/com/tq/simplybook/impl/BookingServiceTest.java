package com.tq.simplybook.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.ClientData;
import com.tq.simplybook.req.GetBookingReq;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.resp.GetBookingResp;
import com.tq.simplybook.utils.SbmExecute;

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
		assertNotNull(info.getCompany_login());
	}

	@Test
	public void testGetListBooking() throws SbmSDKException {
		String companyLogin = "canhcanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);

		GetBookingReq e = new GetBookingReq("2018-05-24", "non_cancelled", "start_date", 7, 1);
		List<GetBookingResp> bookingList = bookingService.getBookings(companyLogin, endpoint, userToken, e);
		assertTrue(bookingList.size() > 0);

	}

	@Test
	public void testCancelBatch() throws SbmSDKException {
		String companyLogin = "truequit";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		List<Long> bookingId = Arrays.asList(142L);
		boolean a = bookingService.cancelBatch(companyLogin, endpoint, userToken, "10", bookingId);
		assertTrue(a);
	}

	@Test
	public void testBooking() throws Exception {
		String companyLogin = "canhcanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		ClientData clientData = new ClientData("Suong", "jasmine@gmail.com", "01225543977");
		String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, userToken, "book",
				new ArrayList<>(Arrays.asList(1, 7,69, "2018-06-06", "10:00:00", 1, clientData )));
		System.out.println(jsonResp);
	}
	@Test
	public void testGetCompanyInfo() throws Exception {
		String companyLogin = "canhcanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, userToken, "getCompanyInfo",
				new ArrayList<String>());
		System.out.println(jsonResp);
	}
}
