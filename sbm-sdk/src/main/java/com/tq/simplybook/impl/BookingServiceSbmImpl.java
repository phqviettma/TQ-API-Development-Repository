package com.tq.simplybook.impl;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.ParamIdReq;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.resp.BookingDetails;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.utils.SbmExecute;
import com.tq.simplybook.utils.SbmUtils;

public class BookingServiceSbmImpl implements BookingServiceSbm {

	@Override
	public BookingInfo getBookingInfo(String companyLogin, String endpoint, String userToken, Long id) throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, userToken, "getBookingDetails", new ParamIdReq(id));
			BookingDetails readValueForObject = SbmUtils.readValueForObject(jsonResp, BookingDetails.class);
			return readValueForObject.getResult();
		} catch (Exception e) {
			throw new SbmSDKException(e);
		}
	}
	
}
