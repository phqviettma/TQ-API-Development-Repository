package com.tq.simplybook.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.GetUnitListReq;
import com.tq.simplybook.resp.CompanyTimeZone;
import com.tq.simplybook.resp.TimeZoneDetail;
import com.tq.simplybook.resp.UnitInfoDetail;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.service.UnitServiceSbm;
import com.tq.simplybook.utils.SbmExecute;
import com.tq.simplybook.utils.SbmUtils;

public class UnitServiceSbmImpl implements UnitServiceSbm {
	private static final Logger m_log = LoggerFactory.getLogger(UnitServiceSbmImpl.class);
	@Override
	public List<UnitProviderInfo> getUnitList(String companyLogin, String endpoint, String token, Boolean isVisibleOnly,
			Boolean asArray, Integer handleClasses) throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, token, "getUnitList",
					new GetUnitListReq(isVisibleOnly, asArray, handleClasses));
			m_log.info("getUnitList json response: "+ String.valueOf(jsonResp));
			UnitInfoDetail readValueForObject = SbmUtils.readValueForObject(jsonResp, UnitInfoDetail.class);

			return readValueForObject.getResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during getUnitList()", e);
		}

	}

	@Override
	public CompanyTimeZone getCompanyTimeZone(String companyLogin, String endpoint, String token)
			throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, token, "getCompanyTimezoneOffset",
					new ArrayList<String>());

			TimeZoneDetail readValueForObject = SbmUtils.readValueForObject(jsonResp, TimeZoneDetail.class);
			return readValueForObject.getResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during getCompanyTimezoneOffset()", e);
		}

	}

}
