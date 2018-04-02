package com.tq.googlecalendar.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;
import com.tq.common.lambda.dynamodb.service.GoogleCalRenewService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleConnectStatusResponse;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.req.StopWatchEventReq;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.googlecalendar.utils.GoogleCalendarUtil;

public class GoogleDisconnectCalendarHandler implements Handler {
	private static final Logger m_log = LoggerFactory.getLogger(GoogleDisconnectCalendarHandler.class);

	private Env eVariables = null;
	private GoogleCalendarDbService googleCalendarService = null;
	private TokenGoogleCalendarService tokenCalendarService = new TokenGoogleCalendarImpl();
	private GoogleCalendarApiServiceBuilder apiServiceBuilder = null;
	private GoogleCalRenewService googleCalRenewService = null;
	private GoogleCalendarModifiedSyncService calendarModifiedChannelService = null;

	public GoogleDisconnectCalendarHandler(Env eVariables, GoogleCalendarDbService googleCalendarService,
			TokenGoogleCalendarService tokenCalendarService, GoogleCalendarApiServiceBuilder apiServiceBuilder,
			GoogleCalRenewService googleCalRenewService,
			GoogleCalendarModifiedSyncService calendarModifiedChannelService) {
		this.eVariables = eVariables;
		this.googleCalendarService = googleCalendarService;
		this.tokenCalendarService = tokenCalendarService;
		this.apiServiceBuilder = apiServiceBuilder;
		this.googleCalRenewService = googleCalRenewService;
		this.calendarModifiedChannelService = calendarModifiedChannelService;
	}

	@Override
	public GoogleConnectStatusResponse handle(GoogleRegisterReq req)
			throws GoogleApiSDKException, TrueQuitRegisterException {
		GoogleConnectStatusResponse response = new GoogleConnectStatusResponse();
		String sbmEmail = req.getParams().getEmail();
		GoogleCalendarSbmSync googleCalendarSbmSync = googleCalendarService.query(sbmEmail);
		if (googleCalendarSbmSync != null) {
			TokenReq tokenReq = new TokenReq(eVariables.getGoogleClientId(), eVariables.getGoogleClientSecrets(),
					googleCalendarSbmSync.getRefreshToken());

			TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);
			StopWatchEventReq stopEventReq = new StopWatchEventReq(googleCalendarSbmSync.getSbmId(),
					googleCalendarSbmSync.getGcWatchResourceId());
			GoogleCalendarApiService googleApiService = apiServiceBuilder.build(tokenResp.getAccess_token());
			boolean flag = false;
			// work-around: can' stop channel in just one request. So as to make sure the
			// channel is stopped successfully, we try to stop maximum of 3 times .
			boolean isChecked = GoogleCalendarUtil.stopWatchChannel(googleApiService, stopEventReq, flag);
			if (isChecked) {
				googleCalendarService.delete(googleCalendarSbmSync);
				m_log.info("Delete record in table GoogleCalendarSbmSync successfully");
				GoogleRenewChannelInfo renewChannel = googleCalRenewService.query(googleCalendarSbmSync.getSbmId());
				if (renewChannel != null) {
					googleCalRenewService.deleteItem(renewChannel);
					m_log.info("Delete record in table GoogleRenewChannelInfo successfully");
					m_log.info("Channel Id" + googleCalendarSbmSync.getSbmId());
					calendarModifiedChannelService.deleteDynamoItem(googleCalendarSbmSync.getSbmId());
					m_log.info("Delete record in table GoogleModified successfully");

				}

			} else {
				throw new GoogleApiSDKException("Internal error");
			}

		} else {
			throw new TrueQuitRegisterException("The email " + sbmEmail + " is not connected yet ");
		}
		response.setSucceeded(true);
		return response;

	}

}
