package com.tq.gcsyncsbm.lambda.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tq.gcsyncsbm.lambda.time.UtcTimeUtil;
import com.tq.simplybook.resp.Breaktime;

public class PractitionerApptGroup {
	private Set<GeneralAppt> appts = new HashSet<GeneralAppt>();
	private Set<String> apptDates = new HashSet<String>();
	private Date startDate = null;
	private Date endDate = null;
	private Map<String, Set<Breaktime>> dateToSbmBreakTimesMap = new HashMap<String, Set<Breaktime>>();

	public Map<String, Set<Breaktime>> getDateToSbmBreakTimesMap() {
		return dateToSbmBreakTimesMap;
	}

	public void addAppt(String date, GeneralAppt appt) {
		this.appts.add(appt);
		this.addDate(date);

		Set<Breaktime> breakTimeSet = dateToSbmBreakTimesMap.get(date);

		if (breakTimeSet == null) {
			breakTimeSet = new HashSet<Breaktime>();
			dateToSbmBreakTimesMap.put(date, breakTimeSet);
		}

		String start_time = UtcTimeUtil.extractTime(appt.getAppointmentStart());
		String end_time = UtcTimeUtil.extractTime(appt.getAppointmentEnd());
		breakTimeSet.add(new Breaktime(start_time, end_time));

	}

	private void addDate(String date) {
		Date newDate = UtcTimeUtil.parseDate(date);
		if (startDate == null || startDate.after(newDate)) {
			startDate = newDate;
		}

		if (endDate == null || endDate.before(newDate)) {
			endDate = newDate;
		}
	}

	public String getStartDateString() {
		DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		return date.format(startDate);
	}

	public String getEndDateString() {
		DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		return date.format(endDate);
	}

	@Override
	public String toString() {
		return "PractitionerApptGroup [appts=" + appts + ", apptDates=" + apptDates + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", dateToSbmBreakTimesMap=" + dateToSbmBreakTimesMap + "]";
	}
}
