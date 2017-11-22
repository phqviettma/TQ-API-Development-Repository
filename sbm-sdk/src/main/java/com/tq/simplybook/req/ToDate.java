package com.tq.simplybook.req;

public class ToDate {
	private String toDate;
	private String toTime;
	
	public ToDate(String toDate, String toTime) {
        super();
        this.toDate = toDate;
        this.toTime = toTime;
    }
    public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	
	@Override
	public String toString() {
		return toDate + " " + toTime;
	}
	
	
}
