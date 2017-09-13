package com.tq.cliniko.service;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.AppointmentsInfo;

public interface ClinikoAppointmentService {
	AppointmentInfo getAppointment(Long id) throws ClinikoSDKExeption;
	AppointmentsInfo getAppointments(String startTime) throws ClinikoSDKExeption;
	AppointmentInfo createAppointment(AppointmentInfo appt) throws ClinikoSDKExeption;
	boolean deleteAppointment(Long id) throws ClinikoSDKExeption;
}
