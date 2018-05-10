package com.tq.simplybook.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.SetWorkDayInfoInfoReq;
import com.tq.simplybook.req.SetWorkDayInfoReq;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.WorkTimeSlot;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.SpecialdayServiceSbm;

public class SbmBreakTimeManagement {

	private SpecialdayServiceSbm sss = new SpecialdayServiceSbmImpl();
	private static final Logger m_log = LoggerFactory.getLogger(SbmBreakTimeManagement.class);
	public static final String START_TAG = "(";
	public static final String END_TAG = ")";
	public static final String SEPARATOR = "-";

	public boolean addBreakTime(String companyLogin, String endpoint, String token, int unit_id, int event_id,
			String envStartWorkingTime, String envEndWorkingTime, String date, Set<Breaktime> newBreakTime,
			Map<String, WorksDayInfoResp> workDayInfoMap) throws SbmSDKException {

		WorksDayInfoResp workDayInfo = workDayInfoMap.get(date);
		Set<WorkTimeSlot> workTimeSlots = workDayInfo.getInfo();
		Set<Breaktime> breakTimes = appenBreakTime(envStartWorkingTime, envEndWorkingTime, newBreakTime, workTimeSlots);

		if (!breakTimes.isEmpty()) {
			m_log.info("Break times to be added for date " + date + ":" + String.valueOf(breakTimes));
			SetWorkDayInfoInfoReq info = new SetWorkDayInfoInfoReq(envStartWorkingTime, envEndWorkingTime, 0,
					breakTimes, 0, date, date, String.valueOf(unit_id), String.valueOf(event_id));
			return sss.changeWorkDay(companyLogin, endpoint, token, new SetWorkDayInfoReq(info));
		} else {
			m_log.info("There is no break times to be added for date " + date);
			return false;
		}
	}

	public boolean removeBreakTime(String companyLogin, String endpoint, String token, int unit_id, int event_id,
			String envStartWorkingTime, String envEndWorkingTime, String date, Set<Breaktime> removedBreakTime,
			Map<String, WorksDayInfoResp> workDayInfoMap, boolean isCliniko) throws SbmSDKException {
		WorksDayInfoResp workDayInfo = workDayInfoMap.get(date);

		if (workDayInfo == null) {
			m_log.info("There is no workdayinfo for date " + date);
			return false;
		} else {

			Set<WorkTimeSlot> workTimeSlots = workDayInfo.getInfo();
			Set<Breaktime> breakTimes;
			breakTimes = removeBreakTime(envStartWorkingTime, envEndWorkingTime, removedBreakTime, workTimeSlots,
					isCliniko);

			if (!breakTimes.isEmpty()) {
				m_log.info("Break times to be removed for date " + date + ":" + String.valueOf(breakTimes));
			} else {
				m_log.info("There is no break times available for date " + date);
			}

			SetWorkDayInfoInfoReq info = new SetWorkDayInfoInfoReq(envStartWorkingTime, envEndWorkingTime, 0,
					breakTimes, 0, date, date, String.valueOf(unit_id), String.valueOf(event_id));
			m_log.info("BreakTime in SbmManagement " + breakTimes);
			boolean isRemoved = sss.changeWorkDay(companyLogin, endpoint, token, new SetWorkDayInfoReq(info));
			if (isRemoved) {
				m_log.info("Unblocked successfully ");
				return true;
			} else {
				return false;
			}
		}

	}

	static Set<Breaktime> appenBreakTime(String envStartWorkingTime, String envEndWorkingTime,
			Set<Breaktime> newBreakTime, Set<WorkTimeSlot> workTimeSlots) {
		Set<Breaktime> currentBreakTimes = findBreakTime(workTimeSlots, envStartWorkingTime, envEndWorkingTime);
		currentBreakTimes.addAll(newBreakTime);
		Set<Breaktime> newBreakTimes = mergeBreakTime(currentBreakTimes);
		return newBreakTimes;

	}

	static Set<Breaktime> removeBreakTime(String envStartWorkingTime, String envEndWorkingTime,
			Set<Breaktime> removedBreakTime, Set<WorkTimeSlot> workTimeSlots, boolean isCliniko) {
		Set<Breaktime> currentBreakTimes = findBreakTime(workTimeSlots, envStartWorkingTime, envEndWorkingTime);
		m_log.info("SBM Current BreakTimes " + currentBreakTimes.toString());
		m_log.info("BreakTimes to be removed " + removedBreakTime.toString());
		Set<Breaktime> remainingBreakTimes;
		if (isCliniko) {
			remainingBreakTimes = SbmBreakTimeManagement.elimateBreakTimes(currentBreakTimes, removedBreakTime);
		} else {
			//removedBreakTime = mergeBreakTime(removedBreakTime);
			remainingBreakTimes = SbmBreakTimeManagement.elimateBreakTimesForGC(currentBreakTimes, removedBreakTime);
		}
		m_log.info("Remaining break times " + remainingBreakTimes.toString());

		return remainingBreakTimes;
	}

	protected static Set<Breaktime> findBreakTime(Set<WorkTimeSlot> workTimeSlot, String envStartWorkingTime,
			String envEndWorkingTime) {
		Set<Breaktime> currentBreakTime = new HashSet<Breaktime>();

		SortedSet<TimePoint> sortedTimePointSet = new SortedTimePointSet();

		TimePoint startTimePoint = new TimePoint(envStartWorkingTime, 0);
		TimePoint endTimePoint = new TimePoint(envEndWorkingTime, 1);

		if (workTimeSlot.isEmpty()) {
			currentBreakTime.add(new Breaktime(startTimePoint.time.toString(), endTimePoint.time.toString()));
		} else {
			sortedTimePointSet.add(startTimePoint);

			workTimeSlotToTimePoints(workTimeSlot, sortedTimePointSet);

			sortedTimePointSet.add(endTimePoint);
			Iterator<TimePoint> it = sortedTimePointSet.iterator();

			Set<TimePointPair> setBreakTimePair = new HashSet<TimePointPair>();

			TimePoint current = it.next();

			while (it.hasNext()) {
				TimePoint next = it.next();

				if ((current.flag == 0 && next.flag == 0) || (current.flag == 1 && next.flag == 0)
						|| (current.flag == 1 && next.flag == 1)) {
					setBreakTimePair.add(new TimePointPair(current, next));
				}

				current = next;
			}

			for (TimePointPair pair : setBreakTimePair) {
				String start = pair.first.time().toString();
				String end = pair.second.time().toString();
				currentBreakTime.add(new Breaktime(start, end));
			}
		}

		return currentBreakTime;
	}

	static Set<Breaktime> elimateBreakTimes(Set<Breaktime> curentBreakTimes, Set<Breaktime> removedBreakTimes) {

		List<Breaktime> retList = new ArrayList<Breaktime>(curentBreakTimes);

		List<Breaktime> tobeRemoveBreakTimes = new ArrayList<Breaktime>();

		for (Breaktime rbt : removedBreakTimes) {
			TimePoint s = new TimePoint(rbt.getStart_time(), 0);
			TimePoint e = new TimePoint(rbt.getEnd_time(), 1);

			for (int i = 0; i < retList.size(); i++) {
				Breaktime breakTime = retList.get(i);

				if (tobeRemoveBreakTimes.contains(breakTime)) {
					continue;
				}

				LocalTime startTime = LocalTime.parse(breakTime.getStart_time());
				LocalTime endTime = LocalTime.parse(breakTime.getEnd_time());

				if (s.time.isBefore(startTime) || s.time.equals(startTime)) {
					if (e.time.isAfter(endTime) || e.time.equals(endTime)) {
						tobeRemoveBreakTimes.add(breakTime);
						continue;
					}
				}
				// 0: means start or end of current breakTime, 1: means start or end of removed
				// breakTime
				// Example: current: 10h-12h removed: 9h-11h so newbreakTime: 11h-12h
				if ((s.time.isBefore(startTime) || s.time.equals(startTime)) && e.time.isAfter(startTime)) {
					// 1 0 1 0
					breakTime.setStart_time(e.time.toString());
				}
				// Example: current: 10h-12h removed: 9h-13h so newbreakTime: 9h-13h
				if (s.time.isBefore(endTime) && (e.time.isAfter(endTime) || e.time.equals(endTime))) {
					// 0 1 0 1
					breakTime.setEnd_time(s.time.toString());
				}
				if (s.time.isAfter(startTime) && e.time.isBefore(endTime)) {
					Breaktime bt1 = new Breaktime();
					bt1.setStart_time(breakTime.getStart_time());
					bt1.setEnd_time(s.time.toString());

					Breaktime bt2 = new Breaktime();
					bt2.setStart_time(e.time.toString());
					bt2.setEnd_time(breakTime.getEnd_time());

					retList.add(bt1);
					retList.add(bt2);

					tobeRemoveBreakTimes.add(breakTime);
				}

			}
		}

		retList.removeAll(tobeRemoveBreakTimes);

		return new HashSet<Breaktime>(retList);
	}

	static Set<Breaktime> elimateBreakTimesForGC(Set<Breaktime> curentBreakTimes, Set<Breaktime> removedBreakTimes) {

		List<Breaktime> retList = new ArrayList<Breaktime>(curentBreakTimes);

		List<Breaktime> tobeRemoveBreakTimes = new ArrayList<Breaktime>();

		for (Breaktime rbt : removedBreakTimes) {
			TimePoint s = new TimePoint(rbt.getStart_time(), 0);
			TimePoint e = new TimePoint(rbt.getEnd_time(), 1);

			for (int i = 0; i < retList.size(); i++) {
				Breaktime breakTime = retList.get(i);

				if (tobeRemoveBreakTimes.contains(breakTime)) {
					continue;
				}

				LocalTime startTime = LocalTime.parse(breakTime.getStart_time());
				LocalTime endTime = LocalTime.parse(breakTime.getEnd_time());

				if (s.time.isBefore(startTime) || s.time.equals(startTime)) {
					if (e.time.isAfter(endTime) || e.time.equals(endTime)) {
						tobeRemoveBreakTimes.add(breakTime);
						continue;
					}
				}
				if (s.time.equals(startTime) || s.time.isAfter(startTime) && e.time.isBefore(endTime)
						|| e.time.equals(endTime)) {
					return new HashSet<Breaktime>(retList);
				}
				// 0: means start or end of current breakTime, 1: means start or end of removed
				// breakTime
				// Example: current: 10h-12h removed: 9h-11h so newbreakTime: 11h-12h
				if ((s.time.isBefore(startTime) || s.time.equals(startTime)) && e.time.isAfter(startTime)) {
					// 1 0 1 0
					breakTime.setStart_time(e.time.toString());
				}
				// Example: current: 10h-12h removed: 9h-13h so newbreakTime: 9h-13h
				if (s.time.isBefore(endTime) && (e.time.isAfter(endTime) || e.time.equals(endTime))) {
					// 0 1 0 1
					breakTime.setEnd_time(s.time.toString());
				}

			}
		}

		retList.removeAll(tobeRemoveBreakTimes);

		return new HashSet<Breaktime>(retList);
	}

	private static void workTimeSlotToTimePoints(Set<WorkTimeSlot> workTimeSlot,
			SortedSet<TimePoint> sortedTimePointSet) {
		for (WorkTimeSlot wts : workTimeSlot) {
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

	public static class TimePoint {

		private final LocalTime time;

		private int flag;// 0 for start, 1 for end

		public TimePoint(String tp, int flag) {
			this.time = LocalTime.parse(tp);
			this.flag = flag;
		}

		@Override
		public String toString() {
			return "TimePoint [time=" + time + ", flag=" + flag + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + flag;
			result = prime * result + ((time == null) ? 0 : time.hashCode());
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
			if (flag != other.flag)
				return false;
			if (time == null) {
				if (other.time != null)
					return false;
			} else if (!time.equals(other.time))
				return false;
			return true;
		}

		public LocalTime time() {
			return this.time;
		}

		public int flag() {
			return this.flag;
		}

	}

	public static class SortedTimePointSet extends TreeSet<TimePoint> {

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

	private static Set<Breaktime> mergeBreakTime(Set<Breaktime> currentBreakTime) {

		Map<LocalTime, List<String>> breakTimeMap = new HashMap();
		int count = 1;

		for (Breaktime breakTime : currentBreakTime) {
			LocalTime startTime = LocalTime.parse(breakTime.getStart_time());
			LocalTime endTime = LocalTime.parse(breakTime.getEnd_time());
			insertValue(breakTimeMap, startTime, count + SEPARATOR + START_TAG);
			insertValue(breakTimeMap, endTime, count + SEPARATOR + END_TAG);
			count++;

		}
		Map<LocalTime, List<String>> treeMap = new TreeMap<>(breakTimeMap);
		Stack<String> stack = new Stack<>();
		Set<Breaktime> newBreakTimes = new HashSet<>();
		LocalTime startPoint = null;
		LocalTime endPoint = null;
		for (LocalTime time : treeMap.keySet()) {
			List<String> listValue = treeMap.get(time);
			for (String value : listValue) {
				String tag = value.split(SEPARATOR)[1];
				switch (tag) {
				case START_TAG:
					if (stack.isEmpty()) {
						startPoint = time;
					}
					stack.push(tag);
					break;
				case END_TAG: {

					stack.pop();

					break;
				}
				}

			}
			if (stack.isEmpty()) {
				endPoint = time;
				String startTime = startPoint.toString();
				String endTime = endPoint.toString();
				Breaktime breakTime = new Breaktime(startTime, endTime);
				newBreakTimes.add(breakTime);
			}

		}

		return newBreakTimes;
	}

	private static void insertValue(Map<LocalTime, List<String>> breakTimeMap, LocalTime time, String tag) {
		List<String> valueList = null;
		if (breakTimeMap.containsKey(time)) {
			valueList = breakTimeMap.get(time);
		} else {
			valueList = new ArrayList<>();
		}
		valueList.add(tag);
		breakTimeMap.put(time, valueList);
	}
}
