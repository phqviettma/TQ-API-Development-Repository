package com.tq.simplybook.impl;

import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.UnitProviderInfo;

public class GetUnitInfoTest {
	private TokenServiceImpl tokenService = new TokenServiceImpl();
	private UnitServiceSbmImpl unitService = new UnitServiceSbmImpl();

	
	@Test
	public void testGetUnitList() throws SbmSDKException {
		
		String companyLogin = "trancanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "unYpE7U7Es";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		List<UnitProviderInfo> info = unitService.getUnitList(companyLogin, endpoint, userToken, true, true, 1);
		assertNotNull(info);
		for(UnitProviderInfo unitInfo : info) {
		
			Set<String> providerInfo = unitInfo.getEvent_map().keySet();
			for(Iterator<String>it = providerInfo.iterator();it.hasNext();) {
				String providerId = it.next();
				String unitId = unitInfo.getId();
				String sbmId = providerId+","+unitId;
				
			}
		}
	}
}
