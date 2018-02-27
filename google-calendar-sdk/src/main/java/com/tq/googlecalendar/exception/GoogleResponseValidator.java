package com.tq.googlecalendar.exception;

import java.io.Serializable;

import com.tq.googlecalendar.resp.CalendarEvents;
import com.tq.googlecalendar.resp.EventResp;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.resp.WatchEventResp;

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
