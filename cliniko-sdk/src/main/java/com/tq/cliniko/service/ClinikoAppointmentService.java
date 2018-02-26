package com.tq.cliniko.service;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.AppointmentsInfo;
import com.tq.cliniko.lambda.model.BusinessesInfo;
import com.tq.cliniko.lambda.model.PractitionersInfo;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.lambda.model.User;

public interface ClinikoAppointmentService {
	AppointmentInfo getAppointment(Long id) throws ClinikoSDKExeption;

	AppointmentsInfo getAppointments(String startTime) throws ClinikoSDKExeption;

	AppointmentsInfo next(AppointmentsInfo apptInfo) throws ClinikoSDKExeption;

	AppointmentInfo createAppointment(AppointmentInfo appt) throws ClinikoSDKExeption;

	boolean deleteAppointment(Long id) throws ClinikoSDKExeption;

	Settings getAllSettings() throws ClinikoSDKExeption;

	AppointmentsInfo getAppointmentInfos() throws ClinikoSDKExeption;

	AppointmentsInfo getDeletedAppointments(String startTime) throws ClinikoSDKExeption;

	AppointmentsInfo getCancelAppointments(String startTime) throws ClinikoSDKExeption;

	BusinessesInfo getListBusinesses() throws ClinikoSDKExeption;

	PractitionersInfo getPractitioner(Integer userId) throws ClinikoSDKExeption;

	User getAuthenticateUser() throws ClinikoSDKExeption;

	PractitionersInfo getBusinessPractitioner(String url) throws ClinikoSDKExeption;
}
