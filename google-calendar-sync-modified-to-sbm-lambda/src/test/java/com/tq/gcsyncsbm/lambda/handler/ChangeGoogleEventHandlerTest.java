package com.tq.gcsyncsbm.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.resp.End;
import com.tq.googlecalendar.resp.Items;
import com.tq.googlecalendar.resp.Start;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.resp.UnitWorkingTime;
import com.tq.simplybook.resp.WorkTimeSlot;
import com.tq.simplybook.resp.WorkingTime;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class ChangeGoogleEventHandlerTest {

	private ChangeGoogleEventHandler changeHandler;
	private Env m_env = mock(Env.class);
	private SbmGoogleCalendarDbService m_sbmCalendarService = mock(SbmGoogleCalendarDbService.class);
	private BookingServiceSbm m_bookingService = mock(BookingServiceSbm.class);
	private TokenServiceSbm m_tokenService = mock(TokenServiceSbm.class);
	private SbmUnitService m_unitService = mock(SbmUnitService.class);
	private SpecialdayServiceSbm m_specialdayService = mock(SpecialdayServiceSbm.class);
	private SbmBreakTimeManagement m_sbmBreakTimeManagement = mock(SbmBreakTimeManagement.class);

	@Before
	public void setUp() {
		changeHandler = new ChangeGoogleEventHandler(m_env, m_bookingService, m_sbmCalendarService, m_tokenService, m_unitService, m_specialdayService, m_sbmBreakTimeManagement);
	}
	
	@Test
	public void testBookingChanged() throws SbmSDKException {
		Items event = mock(Items.class);
		Start start = mock(Start.class);
		End end = mock(End.class);
		when(start.getDateTime()).thenReturn("2019-08-20T17:45:00+10:00");
		when(end.getDateTime()).thenReturn("2019-08-20T18:45:00+10:00");
		when(event.getId()).thenReturn("1");
		when(event.getUpdated()).thenReturn("2019-07-18T04:41:20.888Z");
		when(event.getStart()).thenReturn(start);
		when(event.getEnd()).thenReturn(end);
		
		SbmGoogleCalendar sbmGoogleSync = new SbmGoogleCalendar();
		sbmGoogleSync.setSbmId(1L);
		
		BookingInfo bookingInfo = mock(BookingInfo.class);
		when(bookingInfo.getStart_date_time()).thenReturn("2019-08-23 10:30:00");
		when(bookingInfo.getEnd_date_time()).thenReturn("2019-08-23 11:30:00");
		when(bookingInfo.getId()).thenReturn("1");
		when(bookingInfo.getEvent_id()).thenReturn("1");
		when(bookingInfo.getUnit_id()).thenReturn("1");
		when(m_bookingService.getBookingInfo(any(), any(), any(), any())).thenReturn(bookingInfo);
		when(m_bookingService.editBooking(any(), any(), any(), any())).thenReturn(true);
		changeHandler.bookingChanged(event, sbmGoogleSync);
		assertEquals("2019-07-18T04:41:20.888Z", sbmGoogleSync.getUpdated());
		assertEquals("2019-08-20T17:45:00+10:00", sbmGoogleSync.getStartDateTime());
		assertEquals("2019-08-20T18:45:00+10:00", sbmGoogleSync.getEndDateTime());
	}
	
	@Test
	public void testAppointmentChanged() throws SbmSDKException {
		Items event = mock(Items.class);
		Start start = mock(Start.class);
		End end = mock(End.class);
		when(start.getDateTime()).thenReturn("2019-08-20T17:45:00+10:00");
		when(end.getDateTime()).thenReturn("2019-08-20T18:45:00+10:00");
		when(event.getId()).thenReturn("1");
		when(event.getUpdated()).thenReturn("2019-07-18T04:41:20.888Z");
		when(event.getStart()).thenReturn(start);
		when(event.getEnd()).thenReturn(end);
		
		SbmGoogleCalendar sbmGoogleSync = new SbmGoogleCalendar();
		sbmGoogleSync.setSbmId(23L);
		sbmGoogleSync.setUpdated("123");
		sbmGoogleSync.setStartDateTime("2019-08-20T17:46:00+10:00");
		sbmGoogleSync.setEndDateTime("2019-08-20T17:47:00+10:00");
		
		Map<String, UnitWorkingTime> unitWorkingTime = new HashMap<>();
		Map<String, WorkingTime> workingTimeMaps = new HashMap<>();
		WorkingTime workingTimeValue = new WorkingTime("09:00:00", "18:00:00");
		workingTimeMaps.put("23", workingTimeValue);
		UnitWorkingTime workingTime = new UnitWorkingTime("2019-08-20", workingTimeMaps);
		unitWorkingTime.put("2019-08-20", workingTime);
		when(m_unitService.getUnitWorkDayInfo(any(), any(), any(), any(), any(), any())).thenReturn(unitWorkingTime);
		Map<String, WorksDayInfoResp> workDayInfos = new HashMap<>();
		WorksDayInfoResp worksDayInfo = new WorksDayInfoResp();
		worksDayInfo.setDate("2019-08-20");
		Set<WorkTimeSlot> workTimeSlot = new HashSet<>();
		workTimeSlot.add(new WorkTimeSlot("09:00:00", "10:00:00"));
		worksDayInfo.setInfo(workTimeSlot);
		workDayInfos.put("2019-08-20", worksDayInfo);
		FromDate fromDate = new FromDate("2019-08-20", "09:00:00");
		ToDate toDate = new ToDate("2019-08-20", "10:00:00");
		when(m_specialdayService.getWorkDaysInfo("companyLogin", "endpoint", "token", 2, 23, fromDate, toDate))
				.thenReturn(workDayInfos);
		when(m_sbmCalendarService.queryWithIndex(any())).thenReturn(sbmGoogleSync);
		Set<String> dateToBeUpdated = new HashSet<String>();
		changeHandler.appointmentChanged(event, sbmGoogleSync, "2-23", dateToBeUpdated);
		assertEquals("2019-07-18T04:41:20.888Z", sbmGoogleSync.getUpdated());
		assertEquals("2019-08-20T17:45:00+10:00", sbmGoogleSync.getStartDateTime());
		assertEquals("2019-08-20T18:45:00+10:00", sbmGoogleSync.getEndDateTime());
	}
	
}
