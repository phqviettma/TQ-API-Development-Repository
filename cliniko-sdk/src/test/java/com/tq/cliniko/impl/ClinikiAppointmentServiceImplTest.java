package com.tq.cliniko.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.AppointmentsInfo;
import com.tq.cliniko.lambda.model.Settings;

public class ClinikiAppointmentServiceImplTest {
	ClinikiAppointmentServiceImpl m_service = new ClinikiAppointmentServiceImpl("edc98dfa5bff69bc2f4cc5d5af5287cf");

	// @Test
	public void testGetAppointments() throws ClinikoSDKExeption {
		AppointmentsInfo appts = m_service.getAppointments("2013-03-26T14:00Z");
		assertTrue(appts.getAppointments().size() > 0);

		// test no appointments
		appts = m_service.getAppointments("2020-03-26T14:00:00Z");
		assertTrue(appts.getAppointments().isEmpty());
	}

	// @Test
	public void testCreateAppointments() throws ClinikoSDKExeption {
		AppointmentInfo appointmentInfo = new AppointmentInfo();
		appointmentInfo.setAppointment_start("2017-09-11T04:45:00Z");
		appointmentInfo.setPatient_id(45570926);
		appointmentInfo.setAppointment_type_id(247175);
		appointmentInfo.setBusiness_id(56642);
		appointmentInfo.setPractitioner_id(85738);
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
		appointmentInfo.setPatient_id(44083214);
		appointmentInfo.setAppointment_type_id(232334);
		appointmentInfo.setBusiness_id(53724);
		appointmentInfo.setPractitioner_id(80819);
		AppointmentInfo result = m_service.createAppointment(appointmentInfo);
		assertTrue(m_service.deleteAppointment(result.getId()));
	}

	// @Test
	public void tesGetAppointment() throws ClinikoSDKExeption {
		AppointmentInfo appointmentInfo = new AppointmentInfo();
		appointmentInfo.setAppointment_start("2017-11-15T11:00:00Z");
		appointmentInfo.setPatient_id(45570926);
		appointmentInfo.setAppointment_type_id(247175);
		appointmentInfo.setBusiness_id(56642);
		appointmentInfo.setPractitioner_id(85738);
		AppointmentInfo result = m_service.createAppointment(appointmentInfo);
		AppointmentInfo getResult = m_service.getAppointment(result.getId());
		assertNotNull(getResult.getId());
		m_service.deleteAppointment(result.getId());
		getResult = m_service.getAppointment(result.getId());
		assertNull(getResult);
	}

	@Test
	public void testGetAllSetting() throws ClinikoSDKExeption, ParseException {
		Settings account = m_service.getAllSettings();
		String timezone = account.getAccount().getTime_zone();
		TimeZone tz = TimeZone.getTimeZone(account.getAccount().getCountry() + "/" + timezone);
		System.out.println(tz.getOffset(new Date().getTime()) / 1000 / 60 /60);
	
		TimeZone currentTimeZone = TimeZone.getDefault();
		Date now = new Date();
		int offsetFromUtc = currentTimeZone.getOffset(now.getTime()) / 3600000;
		String m2tTimeZoneIs = Integer.toString(offsetFromUtc);
		System.out.println(m2tTimeZoneIs);
		System.out.println(tz.getDSTSavings() / 1000 / 60);
		/*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = formatter.parse("2017-11-20 21:04:00");
		formatter.setTimeZone(TimeZone.getTimeZone("Europe/London"));
		System.out.println(formatter.format(date));*/
		String input = "2017-11-21T12:46:05";
		DateTimeZone timeZone = DateTimeZone.forID( "Asia/Bangkok" );
		DateTime dateTimeIndia = new DateTime( input, timeZone );
		DateTime dateTimeUtcGmt = dateTimeIndia.withZone( DateTimeZone.UTC );
		System.out.println(dateTimeIndia);
		System.out.println(dateTimeUtcGmt);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		formatter.setTimeZone(TimeZone.getTimeZone("Etc/GMT-0"));
		String start_time =formatter.format(dateTimeIndia.toDate());
		System.out.println(start_time);
	}
}
