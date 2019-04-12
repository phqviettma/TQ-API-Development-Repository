package com.tq.cliniko.lambda.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClinikoConnectReqParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4990808343304857797L;
	private String apiKey;
	private String practitionerEmail;
	private Integer practitionerId;
	private Integer businessId;
	private Integer appointmentTypeId;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getPractitionerEmail() {
		return practitionerEmail;
	}

	public void setPractitionerEmail(String practitionerEmail) {
		this.practitionerEmail = practitionerEmail;
	}

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

	public Integer getAppointmentTypeId() {
		return appointmentTypeId;
	}

	public void setAppointmentTypeId(Integer appointmentTypeId) {
		this.appointmentTypeId = appointmentTypeId;
	}

	@Override
	public String toString() {
		return "ClinikoConnectReqParams [apiKey=" + apiKey + ", practitionerEmail=" + practitionerEmail
				+ ", practitionerId=" + practitionerId + ", businessId=" + businessId + ", appointmentTypeId="
				+ appointmentTypeId + "]";
	}
}
