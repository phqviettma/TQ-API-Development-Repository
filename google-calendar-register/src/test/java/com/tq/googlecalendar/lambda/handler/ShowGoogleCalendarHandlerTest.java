package com.tq.googlecalendar.lambda.handler;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleRegisterParams;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.lambda.resp.GoogleConnectStatusResponse;
import com.tq.googlecalendar.resp.GoogleCalendarList;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;

public class ShowGoogleCalendarHandlerTest {
	private TokenGoogleCalendarService googleTokenService = mock(TokenGoogleCalendarService.class);
	private Env env = MockUtil.mockEnv();
	private GoogleCalendarApiServiceBuilder apiServiceBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private ShowGoogleCalendarHandler handler = new ShowGoogleCalendarHandler(googleTokenService, env, apiServiceBuilder);

	@Test
	public void testShowCalendarHandle()
			throws TrueQuitRegisterException, GoogleApiSDKException, SbmSDKException, InfSDKExecption {
		GoogleRegisterReq req = new GoogleRegisterReq();
		req.setAction("show");
		GoogleRegisterParams params = new GoogleRegisterParams("Suong", "Pham", "suongpham53@gmail.com",
				"1/MN08MyNKtzbNA6LnEUzNgDUVerzgGiuc8xScob3w_EA", "jayparkjay34@gmail.com");
		req.setParams(params);
		GoogleCalendarApiService googleApiService = mock(GoogleCalendarApiService.class);
		when(apiServiceBuilder.build(anyString())).thenReturn(googleApiService );
		TokenResp tokenResp = new TokenResp();
		tokenResp.setAccess_token("access_token");
		when(googleTokenService.getToken(any())).thenReturn(tokenResp );
		GoogleCalendarList googleCalendarList = new GoogleCalendarList();
		when(googleApiService.getListCalendar()).thenReturn(googleCalendarList );
		GoogleConnectStatusResponse response = handler.handle(req);
		assertNotNull(response.getStatus());
	}

}
