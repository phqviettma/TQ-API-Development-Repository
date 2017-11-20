package com.tq.simplybook.resp;

import java.io.Serializable;

public class SimplyBookId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1035887252345357564L;
	private String event_id;
	private String unit_id;


	public String getEvent_id() {
		return event_id;
	}

	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}

	public String getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}

	public SimplyBookId(String event_id, String unit_id) {
		this.event_id = event_id;
		this.unit_id = unit_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((event_id == null) ? 0 : event_id.hashCode());
		result = prime * result + ((unit_id == null) ? 0 : unit_id.hashCode());
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
		SimplyBookId other = (SimplyBookId) obj;
		if (event_id == null) {
			if (other.event_id != null)
				return false;
		} else if (!event_id.equals(other.event_id))
			return false;
		if (unit_id == null) {
			if (other.unit_id != null)
				return false;
		} else if (!unit_id.equals(other.unit_id))
			return false;
		return true;
	}

	public SimplyBookId() {
		
	}

	
}
