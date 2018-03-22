package com.tq.googlecalendar.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.req.StopWatchEventReq;
import com.tq.googlecalendar.resp.ErrorResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;

public class GoogleCalendarUtil {
	private static final Logger m_log = LoggerFactory.getLogger(GoogleCalendarUtil.class);

	public static boolean stopWatchChannel(GoogleCalendarApiService googleApiService, StopWatchEventReq stopEventReq,
			boolean flag) throws GoogleApiSDKException {

		for (int i = 0; i <= 3; i++) {
			ErrorResp errorResp = googleApiService.stopWatchEvent(stopEventReq);
			if (errorResp != null) {
				String errorMessage = errorResp.getError().getMessage();
				if (errorMessage.contains("Channel") && errorMessage.contains("not found")) {
					flag = true;
					break;
				} else {
					m_log.info("Internal error");
				}
			}
		}
		return flag;
	}
}
