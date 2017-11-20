package com.tq.simplybook.resp;

import java.io.Serializable;

public class ClinikoId implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8951451599695470067L;
	private Integer practionerId;
	private Integer bussinessId;
	public Integer getPractionerId() {
		return practionerId;
	}
	public void setPractionerId(Integer practionerId) {
		this.practionerId = practionerId;
	}
	public Integer getBussinessId() {
		return bussinessId;
	}
	public void setBussinessId(Integer bussinessId) {
		this.bussinessId = bussinessId;
	}
	public ClinikoId(Integer practionerId, Integer bussinessId) {
		
		this.practionerId = practionerId;
		this.bussinessId = bussinessId;
	}
	public ClinikoId() {
		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bussinessId == null) ? 0 : bussinessId.hashCode());
		result = prime * result + ((practionerId == null) ? 0 : practionerId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClinikoId other = (ClinikoId) obj;
		if (bussinessId == null) {
			if (other.bussinessId != null)
				return false;
		} else if (!bussinessId.equals(other.bussinessId))
			return false;
		if (practionerId == null) {
			if (other.practionerId != null)
				return false;
		} else if (!practionerId.equals(other.practionerId))
			return false;
		return true;
	}
	
	
}
