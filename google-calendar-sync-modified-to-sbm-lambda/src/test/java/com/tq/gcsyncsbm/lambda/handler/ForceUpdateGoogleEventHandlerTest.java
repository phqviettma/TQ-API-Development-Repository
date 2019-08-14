package com.tq.gcsyncsbm.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.resp.CalendarEvents;
import com.tq.googlecalendar.resp.End;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.resp.Items;
import com.tq.googlecalendar.resp.Start;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.UnitWorkingTime;
import com.tq.simplybook.resp.WorkTimeSlot;
import com.tq.simplybook.resp.WorkingTime;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class ForceUpdateGoogleEventHandlerTest {
	private ForceUpdateGoogleEventHandler m_forceHandler;
	private Env m_env = mock(Env.class);
	private GoogleCalendarApiServiceBuilder m_apiServiceBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private SbmGoogleCalendarDbService m_sbmCalendarService = mock(SbmGoogleCalendarDbService.class);
	private SbmBreakTimeManagement m_sbmBreakTimeManagement = mock(SbmBreakTimeManagement.class);
	private GoogleCalendarDbService m_googleCalendarService = mock(GoogleCalendarDbService.class);
	private TokenGoogleCalendarService m_tokenCalendarService = mock(TokenGoogleCalendarService.class);
	private GoogleCalendarModifiedSyncService m_modifiedChannelService = mock(GoogleCalendarModifiedSyncService.class);
	private SbmUnitService m_unitService = mock(SbmUnitService.class);
	private SpecialdayServiceSbm m_specialDayService = mock(SpecialdayServiceSbm.class);
	private TokenServiceSbm m_tokenService = mock(TokenServiceSbm.class);

	@Before
	public void setUp() {
		m_forceHandler = new ForceUpdateGoogleEventHandler(m_env, m_sbmCalendarService, m_apiServiceBuilder,
				m_sbmBreakTimeManagement, m_googleCalendarService, m_tokenCalendarService, m_modifiedChannelService,
				m_unitService, m_specialDayService, m_tokenService);
	}
	
	@Test
	public void testHandle() throws NumberFormatException, GoogleApiSDKException, SbmSDKException {
		GCModifiedChannel channelInfo = mock(GCModifiedChannel.class);
		when(channelInfo.getEmail()).thenReturn("ntanh7843@gmail.com");
		GoogleCalendarSbmSync googleCalendarInfo = mock(GoogleCalendarSbmSync.class);
		when(googleCalendarInfo.getRefreshToken()).thenReturn("refreshToken");
		when(googleCalendarInfo.getGoogleCalendarId()).thenReturn("ntanh7843@gmail.com");
		when(googleCalendarInfo.getSbmId()).thenReturn("2-23");

		List<GoogleCalendarSbmSync> googleCalendarInfos = new ArrayList<GoogleCalendarSbmSync>();
		googleCalendarInfos.add(googleCalendarInfo);
		
		when(m_modifiedChannelService.load(any())).thenReturn(channelInfo);
		when(m_googleCalendarService.queryEmail(any())).thenReturn(googleCalendarInfos);
		
		Set<String> dateToBeUpdated = new HashSet<String>();
		dateToBeUpdated.add("2019-08-20");
		String googleCalendarId = "ntanh7843@gmail.com";
		
		TokenResp token = mock(TokenResp.class);
		when(m_tokenCalendarService.getToken(any())).thenReturn(token);
		
		GoogleCalendarApiService googleApiService = mock(GoogleCalendarApiService.class);
		GoogleCalendarSettingsInfo settingInfo = mock(GoogleCalendarSettingsInfo.class);
		when(settingInfo.getValue()).thenReturn("Australia/Sydney");
		when(googleApiService.getSettingInfo(any())).thenReturn(settingInfo);
		when(m_apiServiceBuilder.build(any())).thenReturn(googleApiService);
		
		Items event = mock(Items.class);
		Start start = mock(Start.class);
		End end = mock(End.class);
		when(start.getDateTime()).thenReturn("2019-08-20T17:45:00+10:00");
		when(end.getDateTime()).thenReturn("2019-08-20T18:45:00+10:00");
		when(event.getId()).thenReturn("1");
		when(event.getUpdated()).thenReturn("2019-07-18T04:41:20.888Z");
		when(event.getStart()).thenReturn(start);
		when(event.getEnd()).thenReturn(end);
		when(event.getStatus()).thenReturn("confirmed");
		
		List<Items> itemList = new ArrayList<Items>();
		itemList.add(event);
		CalendarEvents eventList = mock(CalendarEvents.class);
		when(eventList.getItems()).thenReturn(itemList);
		when(googleApiService.getEventWithoutTokenByTimeMinAndTimeMax(any(), eq("2019-08-20T00:00:00.000+10:00"), eq("2019-08-21T00:00:00.000+10:00"), eq(true), any())).thenReturn(eventList);
		
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
		when(m_specialDayService.getWorkDaysInfo("companyLogin", "endpoint", "token", 2, 23, fromDate, toDate))
				.thenReturn(workDayInfos);
		
		SbmGoogleCalendar sbmGoogleSync = new SbmGoogleCalendar();
		sbmGoogleSync.setSbmId(23L);
		sbmGoogleSync.setUpdated("123");
		sbmGoogleSync.setStartDateTime("2019-08-20T17:46:00+10:00");
		sbmGoogleSync.setEndDateTime("2019-08-20T17:47:00+10:00");
		when(m_sbmCalendarService.queryWithIndex(any())).thenReturn(sbmGoogleSync);
		
		m_forceHandler.handle(googleCalendarId, dateToBeUpdated);
		
		assertEquals("2019-07-18T04:41:20.888Z", sbmGoogleSync.getUpdated());
		assertEquals("2019-08-20T17:45:00+10:00", sbmGoogleSync.getStartDateTime());
		assertEquals("2019-08-20T18:45:00+10:00", sbmGoogleSync.getEndDateTime());
	}
}
