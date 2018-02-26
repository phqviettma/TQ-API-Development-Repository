package com.tq.simplybook.req;

import java.io.Serializable;

public class WorkingDuration implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2356142378319760427L;
	private String dateStart;
	private String dateEnd;
	private int unitGroupId;

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public int getUnitGroupId() {
		return unitGroupId;
	}

	public void setUnitGroupId(int unitGroupId) {
		this.unitGroupId = unitGroupId;
	}

	public WorkingDuration(String dateStart, String dateEnd, int unitGroupId) {

		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.unitGroupId = unitGroupId;
	}

	public WorkingDuration() {

	}

}
