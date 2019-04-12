package com.tq.cliniko.lambda.resp;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tq.cliniko.lambda.model.AppointmentType;
import com.tq.cliniko.lambda.model.Businesses;
import com.tq.cliniko.lambda.model.Practitioner;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClinikoConnectStatusResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1278768251999330621L;
	private boolean succeeded;
	private String status;
	private String data;
	private Map<Integer, Practitioner> practitionersGroupById;
	private Map<Integer, List<Businesses>> businessesGroupByPractitionerId;
	private Map<Integer, List<AppointmentType>> appointmentTypesGroupByPractitioner;

	public boolean isSucceeded() {
		return succeeded;
	}

	public void setSucceeded(boolean succeeded) {
		this.succeeded = succeeded;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}

	public ClinikoConnectStatusResponse(boolean succeeded, String status) {
		this.succeeded = succeeded;
		this.status = status;
	}

	public ClinikoConnectStatusResponse() {
	}

	public Map<Integer, Practitioner> getPractitionersGroupById() {
		return practitionersGroupById;
	}

	public void setPractitionersGroupById(Map<Integer, Practitioner> practitionersGroupById) {
		this.practitionersGroupById = practitionersGroupById;
	}

	public Map<Integer, List<Businesses>> getBusinessesGroupByPractitionerId() {
		return businessesGroupByPractitionerId;
	}

	public void setBusinessesGroupByPractitionerId(Map<Integer, List<Businesses>> businessesGroupByPractitionerId) {
		this.businessesGroupByPractitionerId = businessesGroupByPractitionerId;
	}

	public Map<Integer, List<AppointmentType>> getAppointmentTypesGroupByPractitioner() {
		return appointmentTypesGroupByPractitioner;
	}

	public void setAppointmentTypesGroupByPractitioner(Map<Integer, List<AppointmentType>> appointmentTypesGroupByPractitioner) {
		this.appointmentTypesGroupByPractitioner = appointmentTypesGroupByPractitioner;
	}

	@Override
	public String toString() {
		return "ClinikoConnectStatusResponse [succeeded=" + succeeded + ", status=" + status + ", data=" + data
				+ ", practitionersGroupById=" + practitionersGroupById + ", businessesGroupByPractitionerId="
				+ businessesGroupByPractitionerId + ", appointmentTypesGroupByPractitioner="
				+ appointmentTypesGroupByPractitioner + "]";
	}
	
	
}
