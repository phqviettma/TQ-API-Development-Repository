package com.tq.cliniko.service;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.AppointmentsInfo;
import com.tq.cliniko.lambda.model.BusinessesInfo;
import com.tq.cliniko.lambda.model.ClinikoAppointmentType;
import com.tq.cliniko.lambda.model.Patient;
import com.tq.cliniko.lambda.model.PractitionersInfo;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.lambda.model.User;

public interface ClinikoAppointmentService {
	AppointmentInfo getAppointment(Long id) throws ClinikoSDKExeption;

	AppointmentsInfo getAppointments(String startTime, Integer maxResult, int practitionerId) throws ClinikoSDKExeption;

	AppointmentsInfo next(AppointmentsInfo apptInfo) throws ClinikoSDKExeption;

	AppointmentInfo createAppointment(AppointmentInfo appt) throws ClinikoSDKExeption;

	boolean deleteAppointment(Long id) throws ClinikoSDKExeption;

	Settings getAllSettings() throws ClinikoSDKExeption;

	AppointmentsInfo getAppointmentInfos() throws ClinikoSDKExeption;

	AppointmentsInfo getDeletedAppointments(String startTime, Integer maxResultPerPage, int practitionerId)
			throws ClinikoSDKExeption;

	AppointmentsInfo getCancelAppointments(String startTime, Integer maxResultPerPage, int practitionerId)
			throws ClinikoSDKExeption;

	BusinessesInfo getListBusinesses() throws ClinikoSDKExeption;

	PractitionersInfo getPractitioner(Integer userId) throws ClinikoSDKExeption;

	User getAuthenticateUser() throws ClinikoSDKExeption;

	PractitionersInfo getBusinessPractitioner(String url) throws ClinikoSDKExeption;

	AppointmentsInfo getNewestAppointment(String latestTime, Integer maxResultPerPage, int practitionerId)
			throws ClinikoSDKExeption;

	AppointmentsInfo getNewestDeletedAppointments(String startTime, Integer maxResultPerPage, int practitionerId)
			throws ClinikoSDKExeption;

	AppointmentsInfo getNewestCancelledAppointments(String startTime, Integer maxResultPerPage, int practitionerId)
			throws ClinikoSDKExeption;

	AppointmentsInfo getPractitionerAppointment(Integer practitionerId, Integer maxResultPerPage)
			throws ClinikoSDKExeption;

	Patient createPatient(String firstName, String lastName) throws ClinikoSDKExeption;

	ClinikoAppointmentType getAppointmentType(Integer practitionerId) throws ClinikoSDKExeption;

}
