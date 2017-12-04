package com.tq.cliniko.lambda.model;

import java.util.List;

public class FoundNewApptContext {
	public int count;
	public List<Long> newApptsId;
	public List<AppointmentInfo> newAppts;

	public int getCount() {
		return count;
	}

	public List<Long> getNewApptsId() {
		return newApptsId;
	}

	public List<AppointmentInfo> getNewAppts() {
		return newAppts;
	}

	public FoundNewApptContext(int count, List<Long> newApptsId, List<AppointmentInfo> newAppts) {
		this.count = count;
		this.newApptsId = newApptsId;
		this.newAppts = newAppts;
	}

}