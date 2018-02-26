package com.tq.cliniko.lambda.model;

import java.io.Serializable;

public class ClinikoBusiness implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4905495093079308416L;
	private Integer practitionerId;
	private Integer businessId;

	public Integer getPractitionerId() {
		return practitionerId;
	}

	public void setPractitionerId(Integer practitionerId) {
		this.practitionerId = practitionerId;
	}

	public Integer getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}

	public ClinikoBusiness(Integer practitionerId, Integer businessId) {

		this.practitionerId = practitionerId;
		this.businessId = businessId;
	}

	public ClinikoBusiness() {

	}

	@Override
	public String toString() {
		return "ClinikoBusiness [practitionerId=" + practitionerId + ", businessId=" + businessId + "]";
	}

}
