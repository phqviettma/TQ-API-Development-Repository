package com.tq.googlecalendar.resp;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarEvents implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1843717823963711691L;
	private String kind;
	private String summary;
	private String timeZone;
	private String updated;
	private String nextSyncToken;
	private String nextPageToken;
	private List<Items> items;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getNextPageToken() {
		return nextPageToken;
	}

	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getNextSyncToken() {
		return nextSyncToken;
	}

	public void setNextSyncToken(String nextSyncToken) {
		this.nextSyncToken = nextSyncToken;
	}

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "CalendarEvents [kind=" + kind + ", summary=" + summary + ", timeZone=" + timeZone + ", updated="
				+ updated + ", nextSyncToken=" + nextSyncToken + ", nextPageToken=" + nextPageToken + ", items=" + items
				+ "]";
	}

}
