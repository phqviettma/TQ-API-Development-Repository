package com.tq.simplybook.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.WorkTimeSlot;

public class SbmBreakTimeManagementTest {
    
    @Test
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
        Set<Breaktime> actualBreakTimes = SbmBreakTimeManagement.appenBreakTime("09:00:00", "18:00:00", newBreakTime, workTimeSlot);
        
        Set<Breaktime> expectedBreakTimes = new HashSet<Breaktime>(); 
        expectedBreakTimes.add(new Breaktime("09:00", "10:00"));
        expectedBreakTimes.add(new Breaktime("10:05", "10:10"));
        expectedBreakTimes.add(new Breaktime("11:00", "18:00"));
        assertTrue(actualBreakTimes.containsAll(expectedBreakTimes));
        assertEquals(3, actualBreakTimes.size());
        
        workTimeSlot = new HashSet<WorkTimeSlot>();
        workTimeSlot.add(new WorkTimeSlot("09:00", "18:00"));
        newBreakTime = new HashSet<Breaktime>();
        newBreakTime.add(new Breaktime("12:00", "13:00"));
        actualBreakTimes = SbmBreakTimeManagement.appenBreakTime("09:00", "18:00", newBreakTime, workTimeSlot);
        
        assertTrue(actualBreakTimes.contains(new Breaktime("12:00", "13:00")));
        assertEquals(1, actualBreakTimes.size());
    }
    
    @Test
    public void testRemoveBreakTime() throws SbmSDKException {
        Set<Breaktime> removedBreakTime = new HashSet<Breaktime>(); 
        removedBreakTime.add(new Breaktime("09:00", "10:00"));
        HashSet<WorkTimeSlot> workTimeSlot = new HashSet<WorkTimeSlot>();
        workTimeSlot.add(new WorkTimeSlot("10:00:00", "11:00:00"));
        Set<Breaktime> actuaBreakTimes = SbmBreakTimeManagement.removeBreakTime("09:00:00", "18:00:00", removedBreakTime, workTimeSlot);
        actuaBreakTimes.contains(new Breaktime("11:00", "18:00"));
        assertEquals(1, actuaBreakTimes.size());
    }
    
    
    @Test
    public void testElimateBreakTime() throws SbmSDKException {
        Set<Breaktime> curentBreakTimes = new HashSet<Breaktime>();
		Set<Breaktime> removedBreakTimes = new HashSet<Breaktime>();
		curentBreakTimes.add(new Breaktime("09:00", "12:00"));
		removedBreakTimes.add(new Breaktime("09:00", "10:00"));
		removedBreakTimes.add(new Breaktime("10:00", "11:00"));
		Set<Breaktime> actuaBreakTimes = SbmBreakTimeManagement.elimateBreakTimes(curentBreakTimes, removedBreakTimes);
        //actuaBreakTimes.contains(new Breaktime("11:00", "12:00"));
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

}
