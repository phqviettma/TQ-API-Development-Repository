package com.tq.calendarsbmsync.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.resp.End;
import com.tq.googlecalendar.resp.Items;
import com.tq.googlecalendar.resp.Start;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CreateGoogleCalendarHandlerTest {
	private Env mockEnv = MockUtil.mockEnv();
	private TokenServiceSbm tokenService = mock(TokenServiceSbm.class);
	private SpecialdayServiceSbm specialDayService = mock(SpecialdayServiceSbm.class);
	private SbmGoogleCalendarDbService sbmGoogleCalendarDbService = mock(SbmGoogleCalendarDbService.class);
	private SbmUnitService sbmUnitService = mock(SbmUnitService.class);
	private SbmBreakTimeManagement sbmBreakTimeMangement = new SbmBreakTimeManagement();
	private CreateGoogleCalendarEventHandler createGoogleHandler = new CreateGoogleCalendarEventHandler(mockEnv,
			tokenService, specialDayService, sbmBreakTimeMangement, sbmGoogleCalendarDbService, sbmUnitService);

	@Test
	public void testCreateGoogleHandler() throws SbmSDKException {

		List<Items> item = new ArrayList<>();
		End end = new End("2018-05-26T16:00:00+07:00", "Asia/Saigon");
		Start start = new Start("2018-05-26T15:00:00+07:00", "Asia/Saigon");
		item.add(new Items("calendar#event", "97hc0kp170g6916tpj6a7jved8", "2018-05-22T08:50:36.000Z",
				"2018-05-22T08:53:05.749Z", start, "confirmed", end));
		String sbmId = "1-2";
		when(tokenService.getUserToken(any(),any(),any(),any())).thenReturn("Token");
		SbmGoogleCalendar sbmGoogleCalendar = null;
		when(sbmGoogleCalendarDbService.queryWithIndex(any())).thenReturn(sbmGoogleCalendar);
		createGoogleHandler.handle(item, sbmId);
	}
}
