package com.tq.simplybook.lambda.handler;

import org.junit.Test;

import com.tq.simplybook.lambda.context.Env;
import com.tq.simplybook.resp.ClinikoId;
import com.tq.simplybook.resp.SimplyBookId;

public class HandleSbmClinikoMappingTest {

	private Env m_env = MockUtil.mockEnv();
	private SimplyBookClinikoMapping m_scm = new SimplyBookClinikoMapping(m_env);
	@Test
	public void testPutMapSbmCliniko() {
		SimplyBookId simplybookId = new SimplyBookId("1", "3");
		ClinikoId clinikoId = m_scm.sbmClinikoMapping(simplybookId);
		System.out.println(clinikoId);
	}
	
}
