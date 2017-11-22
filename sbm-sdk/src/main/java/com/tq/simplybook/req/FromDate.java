package com.tq.simplybook.req;

public class FromDate {
	
	public FromDate(String fromDate, String fromTime) {
        super();
        this.fromDate = fromDate;
        this.fromTime = fromTime;
    }

    private String fromDate;
	private String fromTime;
	
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	
	@Override
	public String toString() {
		return fromDate + " " + fromTime;
	}
}
