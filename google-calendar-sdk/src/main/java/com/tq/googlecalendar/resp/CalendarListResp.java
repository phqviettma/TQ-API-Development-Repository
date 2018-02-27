package com.tq.googlecalendar.resp;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarListResp implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2127802641372828568L;
	private String kind;
	private String nextSyncToken;
	private List<Items> items;
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
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
		return "CalendarListResp [kind=" + kind + ", nextSyncToken=" + nextSyncToken + ", items=" + items + "]";
	}
	

}
