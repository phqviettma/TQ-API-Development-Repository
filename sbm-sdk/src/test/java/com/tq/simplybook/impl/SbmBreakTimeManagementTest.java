package com.tq.simplybook.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.WorkTimeSlot;
import com.tq.simplybook.resp.WorksDayInfoResp;

public class SbmBreakTimeManagementTest {
	private static TokenServiceImpl tokenService = new TokenServiceImpl();
	private SpecialdayServiceSbmImpl specialdayService = new SpecialdayServiceSbmImpl();

	// @Test
	public void testFindBreakTime() throws SbmSDKException {
		Set<WorkTimeSlot> workTimeSlot = new HashSet<WorkTimeSlot>();
		workTimeSlot.add(new WorkTimeSlot("09:00:00", "18:00:00"));
		Set<Breaktime> actualBreakTime = SbmBreakTimeManagement.findBreakTime(workTimeSlot, "09:00:00", "18:00:00");
		actualBreakTime.contains(new Breaktime("09:00", "18:00"));

		workTimeSlot = new HashSet<WorkTimeSlot>();
		workTimeSlot.add(new WorkTimeSlot("10:00:00", "11:00:00"));
		workTimeSlot.add(new WorkTimeSlot("13:00:00", "17:00:00"));
		actualBreakTime = SbmBreakTimeManagement.findBreakTime(workTimeSlot, "09:00:00", "18:00:00");
		assertEquals(3, actualBreakTime.size());
		Set<Breaktime> expectedBreakTimes = new HashSet<Breaktime>();
		expectedBreakTimes.add(new Breaktime("09:00", "10:00"));
		expectedBreakTimes.add(new Breaktime("11:00", "13:00"));
		expectedBreakTimes.add(new Breaktime("17:00", "18:00"));
		assertTrue(actualBreakTime.containsAll(expectedBreakTimes));

		workTimeSlot = new HashSet<WorkTimeSlot>();
		actualBreakTime = SbmBreakTimeManagement.findBreakTime(workTimeSlot, "09:00:00", "18:00:00");
		actualBreakTime.contains(new Breaktime("09:00", "18:00"));

		assertEquals(1, actualBreakTime.size());

		workTimeSlot = new HashSet<WorkTimeSlot>();
		workTimeSlot.add(new WorkTimeSlot("09:00", "18:00"));
		actualBreakTime = SbmBreakTimeManagement.findBreakTime(workTimeSlot, "09:00:00", "18:00:00");
		assertEquals(0, actualBreakTime.size());
	}

	@Test
	public void testAppendBreakTime() throws SbmSDKException {
		Set<Breaktime> newBreakTime = new HashSet<Breaktime>();
		newBreakTime.add(new Breaktime("10:05", "10:10"));
		HashSet<WorkTimeSlot> workTimeSlot = new HashSet<WorkTimeSlot>();
		workTimeSlot.add(new WorkTimeSlot("10:00:00", "11:00:00"));
		Set<Breaktime> actualBreakTimes = SbmBreakTimeManagement.appenBreakTime("09:00:00", "18:00:00", newBreakTime,
				workTimeSlot);

		Set<Breaktime> expectedBreakTimes = new HashSet<Breaktime>();
		expectedBreakTimes.add(new Breaktime("09:00", "10:00"));
		expectedBreakTimes.add(new Breaktime("10:05", "10:10"));
		expectedBreakTimes.add(new Breaktime("11:00", "18:00"));
		assertTrue(actualBreakTimes.containsAll(expectedBreakTimes));
		assertEquals(3, actualBreakTimes.size());

		workTimeSlot = new HashSet<WorkTimeSlot>();
		workTimeSlot.add(new WorkTimeSlot("09:00", "19:00"));
		newBreakTime = new HashSet<Breaktime>();
		newBreakTime.add(new Breaktime("14:00", "15:00"));
		actualBreakTimes = SbmBreakTimeManagement.appenBreakTime("09:00", "19:00", newBreakTime, workTimeSlot);
		System.out.println(actualBreakTimes);
		assertTrue(actualBreakTimes.contains(new Breaktime("14:00", "15:00")));
		assertEquals(1, actualBreakTimes.size());

		workTimeSlot = new HashSet<WorkTimeSlot>();
		newBreakTime = new HashSet<Breaktime>();
		newBreakTime.add(new Breaktime("14:00", "15:00"));
		newBreakTime.add(new Breaktime("16:00", "17:00"));
		actualBreakTimes = SbmBreakTimeManagement.appenBreakTime("09:00", "19:00", newBreakTime, workTimeSlot);
		System.out.println(actualBreakTimes);

	}

	// @Test
	public void testRemoveBreakTime() throws SbmSDKException {
		Set<Breaktime> removedBreakTime = new HashSet<Breaktime>();
		removedBreakTime.add(new Breaktime("09:00", "10:00"));
		HashSet<WorkTimeSlot> workTimeSlot = new HashSet<WorkTimeSlot>();
		workTimeSlot.add(new WorkTimeSlot("10:00:00", "11:00:00"));
		Set<Breaktime> actuaBreakTimes = SbmBreakTimeManagement.removeBreakTime("09:00:00", "18:00:00",
				removedBreakTime, workTimeSlot);
		actuaBreakTimes.contains(new Breaktime("11:00", "18:00"));
		assertEquals(1, actuaBreakTimes.size());
	}

	// @Test
	public void testElimateBreakTime() throws SbmSDKException {
		Set<Breaktime> curentBreakTimes = new HashSet<Breaktime>();
		Set<Breaktime> removedBreakTimes = new HashSet<Breaktime>();
		curentBreakTimes.add(new Breaktime("09:00", "12:00"));
		removedBreakTimes.add(new Breaktime("09:00", "10:00"));
		removedBreakTimes.add(new Breaktime("10:00", "11:00"));
		Set<Breaktime> actuaBreakTimes = SbmBreakTimeManagement.elimateBreakTimes(curentBreakTimes, removedBreakTimes);
		// actuaBreakTimes.contains(new Breaktime("11:00", "12:00"));
		assertEquals("11:00", actuaBreakTimes.iterator().next().getStart_time());
		assertEquals("12:00", actuaBreakTimes.iterator().next().getEnd_time());
		assertEquals(1, actuaBreakTimes.size());

		curentBreakTimes = new HashSet<Breaktime>();
		removedBreakTimes = new HashSet<Breaktime>();
		removedBreakTimes.add(new Breaktime("17:00", "18:00"));
		curentBreakTimes.add(new Breaktime("15:00", "16:00"));
		actuaBreakTimes = SbmBreakTimeManagement.elimateBreakTimes(curentBreakTimes, removedBreakTimes);
		assertEquals("15:00", actuaBreakTimes.iterator().next().getStart_time());
		assertEquals("16:00", actuaBreakTimes.iterator().next().getEnd_time());
		assertEquals(1, actuaBreakTimes.size());

		curentBreakTimes = new HashSet<Breaktime>();
		removedBreakTimes = new HashSet<Breaktime>();
		removedBreakTimes.add(new Breaktime("16:00", "18:00"));
		curentBreakTimes.add(new Breaktime("15:00", "17:00"));
		actuaBreakTimes = SbmBreakTimeManagement.elimateBreakTimes(curentBreakTimes, removedBreakTimes);
		assertEquals("15:00", actuaBreakTimes.iterator().next().getStart_time());
		assertEquals("16:00", actuaBreakTimes.iterator().next().getEnd_time());
		assertEquals(1, actuaBreakTimes.size());

		curentBreakTimes = new HashSet<Breaktime>();
		removedBreakTimes = new HashSet<Breaktime>();
		curentBreakTimes.add(new Breaktime("16:00", "18:00"));
		removedBreakTimes.add(new Breaktime("15:00", "17:00"));
		actuaBreakTimes = SbmBreakTimeManagement.elimateBreakTimes(curentBreakTimes, removedBreakTimes);
		assertEquals("17:00", actuaBreakTimes.iterator().next().getStart_time());
		assertEquals("18:00", actuaBreakTimes.iterator().next().getEnd_time());
		assertEquals(1, actuaBreakTimes.size());

		curentBreakTimes = new HashSet<Breaktime>();
		removedBreakTimes = new HashSet<Breaktime>();
		curentBreakTimes.add(new Breaktime("09:00", "18:00"));
		removedBreakTimes.add(new Breaktime("15:00", "16:00"));
		actuaBreakTimes = SbmBreakTimeManagement.elimateBreakTimes(curentBreakTimes, removedBreakTimes);
		Iterator<Breaktime> it = actuaBreakTimes.iterator();

		Breaktime breakTime = it.next();
		assertEquals("16:00", breakTime.getStart_time());
		assertEquals("18:00", breakTime.getEnd_time());

		breakTime = it.next();
		assertEquals("09:00", breakTime.getStart_time());
		assertEquals("15:00", breakTime.getEnd_time());

		assertEquals(2, actuaBreakTimes.size());

		curentBreakTimes = new HashSet<Breaktime>();
		removedBreakTimes = new HashSet<Breaktime>();

		curentBreakTimes.add(new Breaktime("09:00", "18:00"));
		removedBreakTimes.add(new Breaktime("09:00", "18:00"));

		actuaBreakTimes = SbmBreakTimeManagement.elimateBreakTimes(curentBreakTimes, removedBreakTimes);
		assertEquals(0, actuaBreakTimes.size());

		curentBreakTimes = new HashSet<Breaktime>();
		removedBreakTimes = new HashSet<Breaktime>();
		curentBreakTimes.add(new Breaktime("12:00", "15:00"));
		removedBreakTimes.add(new Breaktime("12:00", "13:00"));
		actuaBreakTimes = SbmBreakTimeManagement.elimateBreakTimes(curentBreakTimes, removedBreakTimes);
		assertEquals("13:00", actuaBreakTimes.iterator().next().getStart_time());
		assertEquals("15:00", actuaBreakTimes.iterator().next().getEnd_time());
		assertEquals(1, actuaBreakTimes.size());

		curentBreakTimes = new HashSet<Breaktime>();
		removedBreakTimes = new HashSet<Breaktime>();
		curentBreakTimes.add(new Breaktime("12:00", "15:00"));
		removedBreakTimes.add(new Breaktime("13:00", "15:00"));
		actuaBreakTimes = SbmBreakTimeManagement.elimateBreakTimes(curentBreakTimes, removedBreakTimes);

		assertEquals("12:00", actuaBreakTimes.iterator().next().getStart_time());
		assertEquals("13:00", actuaBreakTimes.iterator().next().getEnd_time());
		assertEquals(1, actuaBreakTimes.size());

		curentBreakTimes = new HashSet<Breaktime>();
		removedBreakTimes = new HashSet<Breaktime>();
		curentBreakTimes.add(new Breaktime("09:00", "15:00"));
		removedBreakTimes.add(new Breaktime("10:00", "11:00"));
		removedBreakTimes.add(new Breaktime("12:00", "13:00"));
		removedBreakTimes.add(new Breaktime("17:00", "18:00"));

		actuaBreakTimes = SbmBreakTimeManagement.elimateBreakTimes(curentBreakTimes, removedBreakTimes);
		assertEquals(3, actuaBreakTimes.size());

		curentBreakTimes = new HashSet<Breaktime>();
		removedBreakTimes = new HashSet<Breaktime>();
		removedBreakTimes.add(new Breaktime("10:00", "11:00"));

		actuaBreakTimes = SbmBreakTimeManagement.elimateBreakTimes(curentBreakTimes, removedBreakTimes);
		assertTrue(actuaBreakTimes.isEmpty());
	}

	@Test
	public void testAddBreakTime() throws SbmSDKException {
		SbmBreakTimeManagement sbm = new SbmBreakTimeManagement();
		String companyLogin = "truequit";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "epymutehy";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		Set<Breaktime> newBreakTime = new HashSet<>();
		newBreakTime.add(new Breaktime("14:00", "15:00"));
		newBreakTime.add(new Breaktime("16:00", "17:00"));
		Map<String, WorksDayInfoResp> workDayInfoMap = specialdayService.getWorkDaysInfo(companyLogin, endpoint,
				userToken, 4, 2, new FromDate("2018-03-22", "09:00"), new ToDate("2018-03-22", "19:00"));
		sbm.addBreakTime(companyLogin, endpoint, userToken, 4, 2, "09:00", "19:00", "2018-03-22", newBreakTime,
				workDayInfoMap);
	}

}
