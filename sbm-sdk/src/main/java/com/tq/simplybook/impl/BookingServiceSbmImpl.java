package com.tq.simplybook.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.CancelBatchReq;
import com.tq.simplybook.req.ParamIdReq;
import com.tq.simplybook.resp.BatchResp;
import com.tq.simplybook.resp.BookingDetails;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.resp.CancelBatchResp;
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
			CancelBatchResp readValueForObject = SbmUtils.readValueForObject(jsonResp, CancelBatchResp.class);
			return readValueForObject.isResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during cancelBatch()", e);
		}

	}
}
