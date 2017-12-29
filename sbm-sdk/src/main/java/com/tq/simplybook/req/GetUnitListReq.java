package com.tq.simplybook.req;

import java.io.Serializable;

public class GetUnitListReq implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3370486754567903708L;
	private Boolean isVisibleOnly;
	private Boolean asArray;
	private Integer handleClasses;
	public Boolean getIsVisibleOnly() {
		return isVisibleOnly;
	}
	public void setIsVisibleOnly(Boolean isVisibleOnly) {
		this.isVisibleOnly = isVisibleOnly;
	}
	public Boolean getAsArray() {
		return asArray;
	}
	public void setAsArray(Boolean asArray) {
		this.asArray = asArray;
	}
	public Integer getHandleClasses() {
		return handleClasses;
	}
	public void setHandleClasses(Integer handleClasses) {
		this.handleClasses = handleClasses;
	}
	
	public GetUnitListReq(Boolean isVisibleOnly, Boolean asArray, Integer handleClasses) {
	
		this.isVisibleOnly = isVisibleOnly;
		this.asArray = asArray;
		this.handleClasses = handleClasses;
	}
	@Override
	public String toString() {
		return "HandleClasses [isVisibleOnly=" + isVisibleOnly + ", asArray=" + asArray + ", handleClasses="
				+ handleClasses + "]";
	}
	
}
