package com.tq.cliniko.lambda.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentsInfo {
	private List<AppointmentInfo> appointments;
	private Next links;
	private Integer total_entries;

	public Integer getTotal_entries() {
		return total_entries;
	}

	public void setTotal_entries(Integer total_entries) {
		this.total_entries = total_entries;
	}

	public Next getLinks() {
		return links;
	}

	public void setLinks(Next links) {
		this.links = links;
	}

	public List<AppointmentInfo> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<AppointmentInfo> appointments) {
		this.appointments = appointments;
	}

	public static boolean hasNext(AppointmentsInfo apptInf) {
		if(apptInf.getLinks().getNext()==null) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "AppointmentsInfo [appointments=" + appointments + ", links=" + links + ", total_entries="
				+ total_entries + "]";
	}


	

}
