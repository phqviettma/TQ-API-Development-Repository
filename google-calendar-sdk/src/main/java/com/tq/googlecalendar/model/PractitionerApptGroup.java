package com.tq.googlecalendar.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.utils.TimeUtils;
import com.tq.googlecalendar.resp.Items;
import com.tq.simplybook.resp.Breaktime;

public class PractitionerApptGroup {
	private Set<GeneralAppt> appts = new HashSet<GeneralAppt>();
	private Set<String> apptDates = new HashSet<String>();
	private Date startDate = null;
	private Date endDate = null;
	private Map<String, EventDateInfo> dateToSbmBreakTimesMap = new HashMap<String, EventDateInfo>();

	public Map<String, EventDateInfo> getEventDateInfoMap() {
		return dateToSbmBreakTimesMap;
	}

	public void addAppt(String date, GeneralAppt appt) {
		this.appts.add(appt);
		this.addDate(date);

		EventDateInfo dateInfo = dateToSbmBreakTimesMap.get(date);

		if (dateInfo == null) {
			dateInfo = new EventDateInfo();
			dateToSbmBreakTimesMap.put(date, dateInfo);
		}
		String start_time = TimeUtils.extractTime(appt.getAppointmentStart());
		String end_time = TimeUtils.extractTime(appt.getAppointmentEnd());

		dateInfo.breakTimeSet.add(new Breaktime(start_time, end_time));
		dateInfo.sbmGoogleCalendar.add(appt.getSbmGoogleCalendar());
		dateInfo.googleEvents.add(appt.getGoogleEvent());

	}

	public Set<GeneralAppt> getAppts() {
		return appts;
	}

	private void addDate(String date) {
		Date newDate = TimeUtils.parseDate(date);
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

	public class EventDateInfo {
		public List<Items> geventList = new ArrayList<>();
		public Set<Breaktime> breakTimeSet = new HashSet<Breaktime>();
		public List<SbmGoogleCalendar> sbmGoogleCalendar = new ArrayList<>();
		public List<Items> googleEvents = new ArrayList<>();
	}
}
