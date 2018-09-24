package com.tq.simplybook.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.CancelBatchReq;
import com.tq.simplybook.req.CancelBookingReq;
import com.tq.simplybook.req.GetBookingReq;
import com.tq.simplybook.req.ParamIdReq;
import com.tq.simplybook.req.SbmConfirmBooking;
import com.tq.simplybook.resp.BatchResp;
import com.tq.simplybook.resp.BookingDetails;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.resp.BookingListResp;
import com.tq.simplybook.resp.CancelBatchResp;
import com.tq.simplybook.resp.GetBookingResp;
import com.tq.simplybook.resp.SbmConfirmBookingResponse;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.utils.SbmExecute;
import com.tq.simplybook.utils.SbmUtils;

public class BookingServiceSbmImpl implements BookingServiceSbm {
	private static final Logger m_log = LoggerFactory.getLogger(BookingServiceSbmImpl.class);

	@Override
	public BookingInfo getBookingInfo(String companyLogin, String endpoint, String userToken, Long id)
			throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, userToken, "getBookingDetails",
					new ParamIdReq(id));
			m_log.info("Json booking detail Response " + String.valueOf(jsonResp));
			BookingDetails readValueForObject = SbmUtils.readValueForObject(jsonResp, BookingDetails.class);
			return readValueForObject.getResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during getBookingDetails()", e);
		}
	}

	@Override
	public String createBatch(String companyLogin, String endpoint, String token) throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, token, "createBatch",
					new ArrayList<String>());
			BatchResp readValueForObject = SbmUtils.readValueForObject(jsonResp, BatchResp.class);
			return readValueForObject.getResult();

		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during createBatch()", e);
		}
	}

	@Override
	public boolean cancelBatch(String companyLogin, String endpoint, String token, String batchId, List<Long> bookingId)
			throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, token, "cancelBatch",
					new CancelBatchReq(batchId, bookingId));
			m_log.info("Json response " + jsonResp);
			CancelBatchResp readValueForObject = SbmUtils.readValueForObject(jsonResp, CancelBatchResp.class);
			return readValueForObject.isResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during cancelBatch()", e);
		}

	}

	@Override
	public List<GetBookingResp> getBookings(String companyLogin, String endpoint, String token,
			GetBookingReq getBookingReq) throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, token, "getBookings",
					new ArrayList<>(Arrays.asList(getBookingReq)));
			m_log.info("Json booking list Response " + String.valueOf(jsonResp));
			BookingListResp readValueForObject = SbmUtils.readValueForObject(jsonResp, BookingListResp.class);
			return readValueForObject.getResult();

		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during getBookings()", e);
		}
	}

	@Override
	public boolean setBookingStatus(String companyLogin, String endpoint, String token, Integer bookingId,
			Integer statusId) throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, token, "setStatus",
					new SbmConfirmBooking(bookingId, statusId));
			m_log.info("Json response " + jsonResp);
			SbmConfirmBookingResponse readValueForObject = SbmUtils.readValueForObject(jsonResp,
					SbmConfirmBookingResponse.class);
			return readValueForObject.isResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during set booking status()", e);
		}
	}

	@Override
	public boolean cancelBooking(String companyLogin, String endpoint, String token, Integer bookingId)
			throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, token, "cancelBooking",
					new CancelBookingReq(bookingId));
			m_log.info("Json response " + jsonResp);
			SbmConfirmBookingResponse readValueForObject = SbmUtils.readValueForObject(jsonResp,
					SbmConfirmBookingResponse.class);
			return readValueForObject.isResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during cancel booking", e);
		}
	}

}
