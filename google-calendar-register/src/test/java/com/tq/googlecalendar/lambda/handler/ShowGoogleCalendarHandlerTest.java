package com.tq.googlecalendar.lambda.handler;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleRegisterParams;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.lambda.resp.GoogleConnectStatusResponse;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;

public class ShowGoogleCalendarHandlerTest {
	private TokenGoogleCalendarService googleTokenService = new TokenGoogleCalendarImpl();
	private Env env = MockUtil.mockEnv();
	private ShowGoogleCalendarHandler handler = new ShowGoogleCalendarHandler(googleTokenService, env);

	@Test
	public void testShowCalendarHandle()
			throws TrueQuitRegisterException, GoogleApiSDKException, SbmSDKException, InfSDKExecption {
		GoogleRegisterReq req = new GoogleRegisterReq();
		req.setAction("show");
		GoogleRegisterParams params = new GoogleRegisterParams("Suong", "Pham", "suongpham53@gmail.com",
				"1/MN08MyNKtzbNA6LnEUzNgDUVerzgGiuc8xScob3w_EA", "jayparkjay34@gmail.com");
		req.setParams(params);
		GoogleConnectStatusResponse response = handler.handle(req);
		assertNotNull(response.getStatus());
	}

}
