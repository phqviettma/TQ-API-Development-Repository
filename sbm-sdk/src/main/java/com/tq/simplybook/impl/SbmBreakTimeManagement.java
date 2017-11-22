package com.tq.simplybook.impl;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.SetWorkDayInfoInfoReq;
import com.tq.simplybook.req.SetWorkDayInfoReq;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.WorkTimeSlot;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.SpecialdayServiceSbm;

public class SbmBreakTimeManagement {

    private SpecialdayServiceSbm sss = new SpecialdayServiceSbmImpl();

    public boolean addBreakTime(String companyLogin, String endpoint, String token, int unit_id, int event_id, String envStartWorkingTime,
            String envEndWorkingTime, String date, Set<Breaktime> newBreakTime, Map<String, WorksDayInfoResp> workDayInfoMap) throws SbmSDKException {

        WorksDayInfoResp workDayInfo = workDayInfoMap.get(date);
        Set<WorkTimeSlot> workTimeSlots = workDayInfo.getInfo();

        Set<Breaktime> breakTimes = appenBreakTime(envStartWorkingTime, envEndWorkingTime, newBreakTime, workTimeSlots);
        
        SetWorkDayInfoInfoReq info = new SetWorkDayInfoInfoReq(envStartWorkingTime, envEndWorkingTime, 0, breakTimes, 0, null, date, String.valueOf(unit_id),
                String.valueOf(event_id));

        return sss.changeWorkDay(companyLogin, endpoint, token, new SetWorkDayInfoReq(info));
    }

    public boolean removeBreakTime(String companyLogin, String endpoint, String token, int unit_id, int event_id, String envStartWorkingTime,
            String envEndWorkingTime, String date, Set<Breaktime> removedBreakTime, Map<String, WorksDayInfoResp> workDayInfoMap) throws SbmSDKException {
        
        WorksDayInfoResp workDayInfo = workDayInfoMap.get(date);
        Set<WorkTimeSlot> workTimeSlots = workDayInfo.getInfo();
        
        Set<Breaktime> breakTimes = removeBreakTime(envStartWorkingTime, envEndWorkingTime, removedBreakTime, workTimeSlots);
        
        SetWorkDayInfoInfoReq info = new SetWorkDayInfoInfoReq(envStartWorkingTime, envEndWorkingTime, 0, breakTimes, 0, null, date,
                String.valueOf(unit_id), String.valueOf(event_id));

        return sss.changeWorkDay(companyLogin, endpoint, token, new SetWorkDayInfoReq(info));
    }
    
    static Set<Breaktime> appenBreakTime(String envStartWorkingTime, String envEndWorkingTime, Set<Breaktime> newBreakTime, Set<WorkTimeSlot> workTimeSlots){
        Set<Breaktime> currentBreakTimes = findBreakTime(workTimeSlots, envStartWorkingTime, envEndWorkingTime);
        
        currentBreakTimes.addAll(newBreakTime);
        return currentBreakTimes;
    }
    
    
    static Set<Breaktime> removeBreakTime(String envStartWorkingTime, String envEndWorkingTime, Set<Breaktime> removedBreakTime, Set<WorkTimeSlot> workTimeSlots) {
        Set<Breaktime> currentBreakTimes = findBreakTime(workTimeSlots, envStartWorkingTime, envEndWorkingTime);
        
        currentBreakTimes.removeAll(removedBreakTime);
        
        return currentBreakTimes;
    }

    protected static Set<Breaktime> findBreakTime(Set<WorkTimeSlot> workTimeSlot, String envStartWorkingTime, String envEndWorkingTime)  {
        Set<Breaktime> currentBreakTime = new HashSet<Breaktime>();
        
        SortedSet<TimePoint> sortedTimePointSet = new SortedTimePointSet();
        
        sortedTimePointSet.add(new TimePoint(envStartWorkingTime, 0));
        
        workTimeSlotToTimePoints(workTimeSlot, sortedTimePointSet);
        
        sortedTimePointSet.add(new TimePoint(envEndWorkingTime, 1));
        Iterator<TimePoint> it = sortedTimePointSet.iterator();
        
        Set<TimePointPair> setBreakTimePair = new HashSet<TimePointPair>();
        
        TimePoint current = it.next();
        
        while(it.hasNext()) {
            TimePoint next = it.next();
            
            if((current.flag == 0 && next.flag == 0) ||  (current.flag == 1 && next.flag == 0)
                    || (current.flag == 1 && next.flag == 1)){
                setBreakTimePair.add(new TimePointPair(current, next));
            } 
            
            current = next;
        }
        
        for(TimePointPair pair : setBreakTimePair) {
            String start = pair.first.time().toString();
            String end = pair.second.time().toString();
            currentBreakTime.add(new Breaktime(start, end));
        }
        
        return currentBreakTime;
    }
    
    private static void workTimeSlotToTimePoints(Set<WorkTimeSlot> workTimeSlot, SortedSet<TimePoint> sortedTimePointSet) {
        for(WorkTimeSlot wts : workTimeSlot) {
            sortedTimePointSet.add(new TimePoint(wts.getFrom(), 0));
            sortedTimePointSet.add(new TimePoint(wts.getTo(), 1));
        }
    }
    
    private static class TimePointPair {
        public TimePoint first;
        public TimePoint second;
        
        public TimePointPair(TimePoint first, TimePoint second) {
            this.first = first;
            this.second = second;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((first == null) ? 0 : first.hashCode());
            result = prime * result + ((second == null) ? 0 : second.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TimePointPair other = (TimePointPair) obj;
            if (first == null) {
                if (other.first != null)
                    return false;
            } else if (!first.equals(other.first))
                return false;
            if (second == null) {
                if (other.second != null)
                    return false;
            } else if (!second.equals(other.second))
                return false;
            return true;
        }
    }
    
    private static class TimePoint {

        private final LocalTime time;
        
        private final int flag;// 0 for start, 1 for end
        
        @Override
        public String toString() {
            return "TimePoint [Time=" + time + ", flag=" + flag + "]";
        }

        public TimePoint(String tp, int flag)  {
            this.time = LocalTime.parse(tp);
            this.flag = flag;
        }

        public LocalTime time() {
            return this.time;
        }
        
        public int flag(){
            return this.flag;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((time == null) ? 0 : time.hashCode());
            result = prime * result + flag;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TimePoint other = (TimePoint) obj;
            if (time == null) {
                if (other.time != null)
                    return false;
            } else if (!time.equals(other.time))
                return false;
            if (flag != other.flag)
                return false;
            return true;
        }
        
        
    }

    private static class SortedTimePointSet extends TreeSet<TimePoint> {
        
        public SortedTimePointSet() {
            super(new Comparator<TimePoint>() {

                @Override
                public int compare(TimePoint o1, TimePoint o2) {
                    if (o1.time().equals(o2.time())) {
                        return 0;
                    } else if (o1.time().isAfter(o2.time())) {
                        return 1;
                    } else {
                        return -1;
                    }

                }

            });
        }
    }

}
