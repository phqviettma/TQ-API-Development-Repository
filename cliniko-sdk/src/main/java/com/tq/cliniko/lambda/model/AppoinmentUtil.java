package com.tq.cliniko.lambda.model;

public class AppoinmentUtil {

	public static int getBusinessId(AppointmentInfo info) {
		String[] elements = info.getBusiness().getLinks().getSelf().split("/");
		return Integer.valueOf(elements[elements.length - 1]);
	}

	public static int getPractitionerId(AppointmentInfo info) {
		String[] elements = info.getPractitioner().getLinks().getSelf().split("/");
		return Integer.valueOf(elements[elements.length - 1]);
	}

}
