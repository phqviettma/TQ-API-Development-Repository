package com.tq.cliniko.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.model.AppoinmentUtil;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.AppointmentsInfo;
import com.tq.cliniko.lambda.model.Businesses;
import com.tq.cliniko.lambda.model.BusinessesInfo;
import com.tq.cliniko.lambda.model.ClinikoAppointmentType;
import com.tq.cliniko.lambda.model.Patient;
import com.tq.cliniko.lambda.model.PractitionersInfo;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.lambda.model.User;

public class ClinikiAppointmentServiceImplTest {
	static ClinikiAppointmentServiceImpl m_service = new ClinikiAppointmentServiceImpl(
			"ff997f7d491b555f227262870a2717c1");
	Integer maxResult = 100;

	@Test
	public void testGetAppointments() throws ClinikoSDKExeption {
		AppointmentsInfo appts = m_service.getAppointments("2017-11-25T14:00Z", maxResult, 95260);
		System.out.println(appts.getAppointments());
		System.out.println(appts.getLinks().getNext());
		assertTrue(AppoinmentUtil.getBusinessId(appts.getAppointments().get(0)) > 0);
		assertTrue(appts.getAppointments().size() > 0);

		// test no appointments
		appts = m_service.getAppointments("2020-03-26T14:00:00Z", maxResult, 95260);
		assertTrue(appts.getAppointments().isEmpty());
	}

	@Test
	public void testGetDeletedAppointments() throws ClinikoSDKExeption {
		AppointmentsInfo appts = m_service.getDeletedAppointments("2017-11-28T00:00Z", maxResult, 95260);
		System.out.println(appts.getAppointments());
		assertTrue(AppoinmentUtil.getBusinessId(appts.getAppointments().get(0)) > 0);

	}

	@Test
	public void testGetCancelledAppointment() throws ClinikoSDKExeption {
		AppointmentsInfo appts = m_service.getCancelAppointments("2017-12-04T00:00Z", maxResult, 95260);
		System.out.println(appts.getAppointments());
		assertTrue(AppoinmentUtil.getBusinessId(appts.getAppointments().get(0)) > 0);

	}

	@Test
	public void testCreateAppointments() throws ClinikoSDKExeption {
		AppointmentInfo appointmentInfo = new AppointmentInfo();
		appointmentInfo.setAppointment_start("2017-09-11T04:45:00Z");
		appointmentInfo.setPatient_id(47508555);
		appointmentInfo.setAppointment_type_id(270071);
		appointmentInfo.setBusiness_id(60602);
		appointmentInfo.setPractitioner_id(92699);
		AppointmentInfo result = m_service.createAppointment(appointmentInfo);
		assertEquals("2017-09-11T04:45:00Z", result.getAppointment_start());
		assertNotNull(result.getId());

		// test appointment no created
		// wrong date input
		appointmentInfo.setAppointment_start("2017-09-32T04:45:00Z");
		result = m_service.createAppointment(appointmentInfo);
		assertNull(result.getId());
		assertNull(result.getAppointment_start());
	}

	// @Test
	public void testDeleteAppointment() throws ClinikoSDKExeption {
		AppointmentInfo appointmentInfo = new AppointmentInfo();
		appointmentInfo.setAppointment_start("2017-09-11T04:45:00Z");
		appointmentInfo.setPatient_id(46101691);
		appointmentInfo.setAppointment_type_id(252503);
		appointmentInfo.setBusiness_id(57535);
		appointmentInfo.setPractitioner_id(87313);
		AppointmentInfo result = m_service.createAppointment(appointmentInfo);
		assertTrue(m_service.deleteAppointment(result.getId()));
	}

	@Test
	public void tesGetAppointment() throws ClinikoSDKExeption {
		AppointmentInfo appointmentInfo = new AppointmentInfo();
		appointmentInfo.setAppointment_start("2017-11-15T11:00:00Z");
		appointmentInfo.setPatient_id(46101691);
		appointmentInfo.setAppointment_type_id(252503);
		appointmentInfo.setBusiness_id(57535);
		appointmentInfo.setPractitioner_id(87313);
		AppointmentInfo result = m_service.createAppointment(appointmentInfo);
		AppointmentInfo getResult = m_service.getAppointment(result.getId());
		assertNotNull(getResult.getId());
		m_service.deleteAppointment(result.getId());
		getResult = m_service.getAppointment(result.getId());
		assertNull(getResult);
	}

	@Test
	public void testGetAuthenticateUser() throws ClinikoSDKExeption {
		User user = m_service.getAuthenticateUser();
		assertNotNull(user.getId());
	}

	@Test
	public void testGetPractitioner() throws ClinikoSDKExeption {
		User user = m_service.getAuthenticateUser();
		PractitionersInfo practitioner = m_service.getPractitioner(user.getId());
		assertNotNull(practitioner.getPractitioners());

	}

	@Test
	public void testGetPractitionerByUrl() throws ClinikoSDKExeption {
		BusinessesInfo businesses = m_service.getListBusinesses();
		for (Businesses business : businesses.getBusinesses()) {
			String link = business.getPractitioners().getLinks().getSelf();
			PractitionersInfo practitionersInfo = m_service.getBusinessPractitioner(link);
			assertNotNull(practitionersInfo.getPractitioners());
		}
	}

	@Test
	public void testGetSystemSettings() throws ClinikoSDKExeption {
		Settings setting = m_service.getAllSettings();
		assertNotNull(setting.getAccount().getTime_zone());
	}

	@Test
	public void testGetPractitionerAppointment() throws ClinikoSDKExeption {
		AppointmentsInfo appt = m_service.getPractitionerAppointment(95260, 10);
		assertTrue(appt.getAppointments().size() > 0);
	}

	@Test
	public void testCreatePatient() throws ClinikoSDKExeption {
		Patient p = m_service.createPatient("Suong", "Pham");
		assertNotNull(p.getId());
	}

	@Test
	public void testGetAppointmentType() throws ClinikoSDKExeption {
		ClinikoAppointmentType apptType = m_service.getAppointmentType(95260);
		assertTrue(apptType.getAppointment_types().size() > 0);
	}

}
