package com.tq.cliniko.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikiAppointmentServiceImpl;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.AppointmentsInfo;

public class ClinikiAppointmentServiceImplTest {
	ClinikiAppointmentServiceImpl m_service = new ClinikiAppointmentServiceImpl("3755b2897cdfb8e479bbbb3251cbd0bc");
	@Test
	public void testGetAppointments() throws ClinikoSDKExeption{
		AppointmentsInfo appts = m_service.getAppointments("2013-03-26T14:00Z");
		assertTrue(appts.getAppointments().size()>0);
		
		//test no appointments
		appts = m_service.getAppointments("2020-03-26T14:00:00Z");
		assertTrue(appts.getAppointments().isEmpty());
	}
	
	@Test
	public void testCreateAppointments() throws ClinikoSDKExeption{
		AppointmentInfo appointmentInfo = new AppointmentInfo();
		appointmentInfo.setAppointment_start("2017-09-11T04:45:00Z");
		appointmentInfo.setPatient_id(44083214);
		appointmentInfo.setAppointment_type_id(232334);
		appointmentInfo.setBusiness_id(53724);
		appointmentInfo.setPractitioner_id(80819);
		AppointmentInfo result = m_service.createAppointment(appointmentInfo);
		assertEquals("2017-09-11T04:45:00Z", result.getAppointment_start());
		assertNotNull(result.getId());
		
		//test appointment no created
		//wrong date input
		appointmentInfo.setAppointment_start("2017-09-32T04:45:00Z");
		result = m_service.createAppointment(appointmentInfo);
		assertNull(result.getId());
		assertNull(result.getAppointment_start());
	}
	
	@Test
	public void testDeleteAppointment() throws ClinikoSDKExeption{
		AppointmentInfo appointmentInfo = new AppointmentInfo();
		appointmentInfo.setAppointment_start("2017-09-11T04:45:00Z");
		appointmentInfo.setPatient_id(44083214);
		appointmentInfo.setAppointment_type_id(232334);
		appointmentInfo.setBusiness_id(53724);
		appointmentInfo.setPractitioner_id(80819);
		AppointmentInfo result = m_service.createAppointment(appointmentInfo);
		assertTrue(m_service.deleteAppointment(result.getId()));
	}
	
	@Test
	public void tesGetAppointment() throws ClinikoSDKExeption{
		AppointmentInfo appointmentInfo = new AppointmentInfo();
		appointmentInfo.setAppointment_start("2017-09-11T04:45:00Z");
		appointmentInfo.setPatient_id(44083214);
		appointmentInfo.setAppointment_type_id(232334);
		appointmentInfo.setBusiness_id(53724);
		appointmentInfo.setPractitioner_id(80819);
		AppointmentInfo result = m_service.createAppointment(appointmentInfo);
		AppointmentInfo getResult = m_service.getAppointment(result.getId());
		assertNotNull(getResult.getId());
		m_service.deleteAppointment(result.getId());
		getResult = m_service.getAppointment(result.getId());
		assertNull(getResult);
	}
}
