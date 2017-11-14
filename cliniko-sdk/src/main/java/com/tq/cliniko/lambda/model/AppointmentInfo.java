package com.tq.cliniko.lambda.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1658636344780671983L;
	private Long id;
	private String appointment_start;
	private String appointment_end;
	private boolean did_not_arrive;
	private String notes;
	private String deleted_at;
	private String cancellation_time;
	private String cancellation_note;
	private String cancellation_reason;
	private int patient_id;
	private int practitioner_id;
	private int appointment_type_id;
	private int business_id;

	public String getAppointment_start() {
		return appointment_start;
	}

	public void setAppointment_start(String appointment_start) {
		this.appointment_start = appointment_start;
	}

	public int getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}

	public int getPractitioner_id() {
		return practitioner_id;
	}

	public void setPractitioner_id(int practitioner_id) {
		this.practitioner_id = practitioner_id;
	}

	public int getAppointment_type_id() {
		return appointment_type_id;
	}

	public void setAppointment_type_id(int appointment_type_id) {
		this.appointment_type_id = appointment_type_id;
	}

	public int getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(int business_id) {
		this.business_id = business_id;
	}

	public String getAppointment_end() {
		return appointment_end;
	}

	public void setAppointment_end(String appointment_end) {
		this.appointment_end = appointment_end;
	}

	public boolean isDid_not_arrive() {
		return did_not_arrive;
	}

	public void setDid_not_arrive(boolean did_not_arrive) {
		this.did_not_arrive = did_not_arrive;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getDeleted_at() {
		return deleted_at;
	}

	public void setDeleted_at(String deleted_at) {
		this.deleted_at = deleted_at;
	}
	
	public String getCancellation_time() {
		return cancellation_time;
	}

	public void setCancellation_time(String cancellation_time) {
		this.cancellation_time = cancellation_time;
	}

	public String getCancellation_note() {
		return cancellation_note;
	}

	public void setCancellation_note(String cancellation_note) {
		this.cancellation_note = cancellation_note;
	}

	public String getCancellation_reason() {
		return cancellation_reason;
	}

	public void setCancellation_reason(String cancellation_reason) {
		this.cancellation_reason = cancellation_reason;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "AppointmentInfo [id=" + id + ", appointment_start=" + appointment_start + ", appointment_end="
				+ appointment_end + ", did_not_arrive=" + did_not_arrive + ", notes=" + notes + ", cancellation_time="
				+ cancellation_time + ", cancellation_note=" + cancellation_note + ", cancellation_reason="
				+ cancellation_reason + ", patient_id=" + patient_id + ", practitioner_id=" + practitioner_id
				+ ", appointment_type_id=" + appointment_type_id + ", business_id=" + business_id + "]";
	}

	

}
