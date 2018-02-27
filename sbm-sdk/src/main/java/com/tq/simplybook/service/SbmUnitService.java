package com.tq.simplybook.service;

import java.util.List;
import java.util.Map;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.resp.UnitWorkingTime;

public interface SbmUnitService {
	public List<UnitProviderInfo> getUnitList(String companyLogin, String endpoint, String token, Boolean isVisibleOnly,
			Boolean asArray, Integer handleClasses) throws SbmSDKException;

	public Map<String, UnitWorkingTime> getUnitWorkDayInfo(String companyLogin, String endpoint, String token,
			String dateStart, String dateEnd, int unitGroupId) throws SbmSDKException;
}
