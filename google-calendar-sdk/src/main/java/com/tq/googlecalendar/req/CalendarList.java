package com.tq.googlecalendar.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4639484462243740268L;
	private Integer maxResult;
	private String minAccessRole;
	private String pageToken;
	private boolean showDeleted;
	private boolean showHidden;
	private String syncToken;

	public Integer getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}

	public String getMinAccessRole() {
		return minAccessRole;
	}

	public void setMinAccessRole(String minAccessRole) {
		this.minAccessRole = minAccessRole;
	}

	public String getPageToken() {
		return pageToken;
	}

	public void setPageToken(String pageToken) {
		this.pageToken = pageToken;
	}

	public boolean isShowDeleted() {
		return showDeleted;
	}

	public void setShowDeleted(boolean showDeleted) {
		this.showDeleted = showDeleted;
	}

	public boolean isShowHidden() {
		return showHidden;
	}

	public void setShowHidden(boolean showHidden) {
		this.showHidden = showHidden;
	}

	public String getSyncToken() {
		return syncToken;
	}

	public void setSyncToken(String syncToken) {
		this.syncToken = syncToken;
	}

	@Override
	public String toString() {
		return "CalendarList [maxResult=" + maxResult + ", minAccessRole=" + minAccessRole + ", pageToken=" + pageToken
				+ ", showDeleted=" + showDeleted + ", showHidden=" + showHidden + ", syncToken=" + syncToken + "]";
	}

}
