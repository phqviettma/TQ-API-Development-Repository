package com.tq.clinikosbmsync.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.FoundNewApptContext;
import com.tq.clinikosbmsync.lamdbda.context.Env;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class ChangeClinikoSyncHandlerTest {
	
	private ChangeClinikoSyncHandler changeHandler = null;
	private SbmClinikoSyncService m_sbmClinikoSyncService = mock(SbmClinikoSyncService.class);
	private BookingServiceSbm m_bookingService = mock(BookingServiceSbm.class);
	private Env m_env = mock(Env.class);
	private TokenServiceSbm m_tokenServiceSbm = mock(TokenServiceSbm.class);
	
	@Before
	public void setUp() {
		changeHandler = new ChangeClinikoSyncHandler(m_env, m_sbmClinikoSyncService, m_bookingService, m_tokenServiceSbm);
	}
	
	@Test
	public void testFindModifiedAppts() throws SbmSDKException {
		List<AppointmentInfo> fetchedAppts = initAppointmentList();
		Set<String> dateToBeUpdated = new HashSet<String>();
		DateTimeZone dateTz = DateTimeZone.forID("Australia/Sydney");
		SbmCliniko sbmClinikoSync1 = new SbmCliniko();
		sbmClinikoSync1.setAgent(ClinikoSyncHandler.CLINIKO);
		sbmClinikoSync1.setAppointmentStart("2019-08-19T23:15:00Z");
		sbmClinikoSync1.setAppointmentEnd("2019-08-20T00:15:00Z");
		sbmClinikoSync1.setUpdatedAt("2019-08-17T10:31:59Z");
		sbmClinikoSync1.setFlag(1);
		
		SbmCliniko sbmClinikoSync2 = new SbmCliniko();
		sbmClinikoSync2.setAgent(ClinikoSyncHandler.SBM);
		sbmClinikoSync2.setAppointmentStart("2019-08-19T23:15:00Z");
		sbmClinikoSync2.setAppointmentEnd("2019-08-20T00:15:00Z");
		sbmClinikoSync2.setUpdatedAt("2019-08-19T10:31:59Z");
		sbmClinikoSync2.setFlag(1);
		
		when(m_sbmClinikoSyncService.queryIndex(1L)).thenReturn(sbmClinikoSync1);
		when(m_sbmClinikoSyncService.queryIndex(2L)).thenReturn(sbmClinikoSync2);
		BookingInfo bookingInfo = new BookingInfo();
		bookingInfo.setStart_date_time("2019-08-21 15:10:00");
		bookingInfo.setEnd_date_time("2019-08-21 16:10:00");
		bookingInfo.setEvent_id("30");
		bookingInfo.setId("2");
		bookingInfo.setUnit_id("30");
		when(m_bookingService.getBookingInfo(any(), any(), any(), any())).thenReturn(bookingInfo);
		when(m_bookingService.editBooking(any(), any(), any(), any())).thenReturn(true);
		FoundNewApptContext result = changeHandler.findModifedAppts(fetchedAppts, dateToBeUpdated, dateTz);
		assertEquals(1, result.getCount());
		assertEquals(true, result.getNewApptsId().contains(1L));
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
