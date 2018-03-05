package com.tq.simplybook.req;

import java.io.Serializable;

public class LocationReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6483333131650983744L;
	private Boolean isPublic;

	public Boolean isPublic() {
		return isPublic;
	}

	public void setPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public LocationReq() {

	}

	public LocationReq(Boolean isPublic) {

		this.isPublic = isPublic;
	}

	@Override
	public String toString() {
		return "LocationReq [isPublic=" + isPublic + "]";
	}

}
