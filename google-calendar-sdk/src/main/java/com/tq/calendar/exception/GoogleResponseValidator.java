package com.tq.calendar.exception;

import java.io.Serializable;

import com.tq.calendar.resp.CalendarEvents;
import com.tq.calendar.resp.EventResp;
import com.tq.calendar.resp.GoogleCalendarSettingsInfo;
import com.tq.calendar.resp.TokenResp;
import com.tq.calendar.resp.WatchEventResp;

public class GoogleResponseValidator {
	public static boolean failed(Serializable o) {
		if (o instanceof GoogleCalendarSettingsInfo) {
			return ((GoogleCalendarSettingsInfo) o).getId() == null;
		}
		if (o instanceof TokenResp) {
			return ((TokenResp) o).getAccess_token() == null;
		}
		if (o instanceof CalendarEvents) {
			return ((CalendarEvents) o).getItems() == null;
		}
		if (o instanceof WatchEventResp) {
			return ((WatchEventResp) o).getId() == null;
		}
		if (o instanceof EventResp) {
			return ((EventResp) o).getId() == null;
		}

		return false;
	}
}
