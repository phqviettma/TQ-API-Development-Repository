package com.tq.simplybook.impl;

import java.util.List;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.GetUnitListReq;
import com.tq.simplybook.resp.UnitInfoDetail;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.service.UnitServiceSbm;
import com.tq.simplybook.utils.SbmExecute;
import com.tq.simplybook.utils.SbmUtils;

public class UnitServiceSbmImpl implements UnitServiceSbm{

	@Override
	public List<UnitProviderInfo> getUnitList(String companyLogin, String endpoint, String token,Boolean isVisibleOnly,Boolean asArray,Integer handleClasses) throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, token, "getUnitList", new GetUnitListReq(isVisibleOnly, asArray, handleClasses));
			UnitInfoDetail readValueForObject = SbmUtils.readValueForObject(jsonResp, UnitInfoDetail.class);
			return readValueForObject.getResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during getUnitList()", e);
		}
		
	}

}
