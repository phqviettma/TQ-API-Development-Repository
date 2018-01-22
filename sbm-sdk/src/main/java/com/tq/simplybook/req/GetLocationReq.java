package com.tq.simplybook.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetLocationReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -471025563895649085L;
	private boolean isPublic;
	private boolean asArray;

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public boolean isAsArray() {
		return asArray;
	}

	public void setAsArray(boolean asArray) {
		this.asArray = asArray;
	}

	@Override
	public String toString() {
		return "GetLocationReq [isPublic=" + isPublic + ", asArray=" + asArray + "]";
	}

	public GetLocationReq(boolean isPublic, boolean asArray) {

		this.isPublic = isPublic;
		this.asArray = asArray;
	}

	public GetLocationReq() {

	}

}
