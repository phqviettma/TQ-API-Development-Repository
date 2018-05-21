package com.tq.googlecalendar.resp;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleCalendarList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6483791201746379325L;
	private String kind;
	private String nextPageToken;
	private String nextSyncToken;
	private List<GoogleCalendarItems> items;

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

	public String getNextSyncToken() {
		return nextSyncToken;
	}

	public void setNextSyncToken(String nextSyncToken) {
		this.nextSyncToken = nextSyncToken;
	}

	public List<GoogleCalendarItems> getItems() {
		return items;
	}

	public void setItems(List<GoogleCalendarItems> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "GoogleCalendarList [kind=" + kind + ", nextPageToken=" + nextPageToken + ", nextSyncToken="
				+ nextSyncToken + ", items=" + items + "]";
	}

}
