package com.tq.simplybook.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingIds implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = -5964259775055651329L;
	private Long ids;

	public Long getIds() {
		return ids;
	}

	public void setIds(Long ids) {
		this.ids = ids;
	}

	@Override
	public String toString() {
		return "BookingIds [ids=" + ids + "]";
	}

	public BookingIds(Long ids) {
		this.ids = ids;
	}

	public BookingIds() {
	}
	
	

}
