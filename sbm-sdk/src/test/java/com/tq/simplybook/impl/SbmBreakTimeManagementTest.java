package com.tq.simplybook.impl;

import static org.junit.Assert.*;

import java.util.HashSet;
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
        assertTrue(actualBreakTime.isEmpty());
        
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
}
