package com.tq.clinikosbmsync.lambda.handler;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTimeZone;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.AppointmentsInfo;
import com.tq.cliniko.lambda.model.FoundNewApptContext;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;

public class ForceUpdateClinikoSyncHandlerTest {
	private ForceUpdateClinikoSyncHandler m_forceHandler = null;
	private ClinikoApiServiceBuilder m_apiServiceBuilder = mock(ClinikoApiServiceBuilder.class);
	private SbmClinikoSyncService m_sbmClinikoSyncService = mock(SbmClinikoSyncService.class);

	@Before
	public void setUp() {
		m_forceHandler = new ForceUpdateClinikoSyncHandler(m_apiServiceBuilder, m_sbmClinikoSyncService);
	}

	@Test
	public void testfindAllAppointmentNeedToBeUpdated() throws ClinikoSDKExeption {
		Set<String> dateToBeUpdated = new HashSet<String>();
		dateToBeUpdated.add("2019-08-20");

		// businessId - practitionerId
		String clinikoId = "125-6858";
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync();
		clinikoSbmSync.setClinikoId(clinikoId);
		clinikoSbmSync.setApiKey("apiKey");

		DateTimeZone dateTz = DateTimeZone.forID("Australia/Sydney");

		ClinikoAppointmentService clinikoApiService = mock(ClinikoAppointmentService.class);
		AppointmentsInfo appointmentsInfo = new AppointmentsInfo();
		List<AppointmentInfo> appointmentList = initAppointmentList();
		appointmentsInfo.setAppointments(appointmentList);
		when(m_apiServiceBuilder.build(any())).thenReturn(clinikoApiService);
		when(clinikoApiService.getAppointmentsByFromDateAndToDate("2019-08-19T14:00:00.000Z",
				"2019-08-20T14:00:00.000Z", 100, 6858)).thenReturn(appointmentsInfo);

		FoundNewApptContext result = m_forceHandler.findAllAppointmentNeedToBeUpdated(dateToBeUpdated, clinikoSbmSync, dateTz);
		assertEquals(2, result.getCount());
	}

	private List<AppointmentInfo> initAppointmentList() {
		List<AppointmentInfo> appointmentList = new ArrayList<AppointmentInfo>();
		AppointmentInfo appointment1 = new AppointmentInfo();
		appointment1.setId(1L);
		appointment1.setAppointment_start("2019-08-20T23:15:00Z");
		appointment1.setAppointment_end("2019-08-21T00:15:00Z");
		appointment1.setUpdated_at("2019-08-20T10:31:58Z");
		appointmentList.add(appointment1);

		AppointmentInfo appointment2 = new AppointmentInfo();
		appointment2.setId(2L);
		appointment2.setAppointment_start("2019-08-21T23:15:00Z");
		appointment2.setAppointment_end("2019-08-22T00:15:00Z");
		appointment2.setUpdated_at("2019-08-21T10:31:58Z");

		appointmentList.add(appointment2);
		return appointmentList;
	}
}
