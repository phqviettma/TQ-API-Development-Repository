package com.tq.googlecalendar.lambda.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
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
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.lambda.resp.GoogleConnectStatusResponse;
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
		List<GoogleCalendarSbmSync> googleCalendarSbmSync = googleCalendarService.queryEmail(sbmEmail);
		boolean stoppedWatch = false;
		if (!googleCalendarSbmSync.isEmpty()) {
			for (GoogleCalendarSbmSync googleSbm : googleCalendarSbmSync) {

				TokenReq tokenReq = new TokenReq(eVariables.getGoogleClientId(), eVariables.getGoogleClientSecrets(),
						googleSbm.getRefreshToken());

				TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);
				StopWatchEventReq stopEventReq = new StopWatchEventReq(googleSbm.getChannelId(),
						googleSbm.getGcWatchResourceId());
				GoogleCalendarApiService googleApiService = apiServiceBuilder.build(tokenResp.getAccess_token());
				// work-around: can' stop channel in just one request. So as to make sure the
				// channel is stopped successfully, we try to stop maximum of 3 times .
			 GoogleCalendarUtil.stopWatchChannel(googleApiService, stopEventReq);	
				stoppedWatch = true;
			}
			if(stoppedWatch) {
				List<GCModifiedChannel> modifiedChannel = calendarModifiedChannelService.queryEmail(sbmEmail); 
				List<GoogleRenewChannelInfo> renewChannel = googleCalRenewService.queryEmail(googleCalendarSbmSync.get(0).getGoogleEmail());
				if (!renewChannel.isEmpty() && !modifiedChannel.isEmpty()) {
					googleCalendarService.deleteGoogleItem(googleCalendarSbmSync);
					m_log.info("Delete record in table GoogleSbmSync successfully");
					googleCalRenewService.deleteRenewChannel(renewChannel);
					calendarModifiedChannelService.deleteModifiedItem(modifiedChannel);
				}
				else {
					throw new GoogleApiSDKException("Internal error");
				}
			}
			else {
				throw new GoogleApiSDKException("Can not stop watch this channel");
			}

		}

		else

		{
			throw new TrueQuitRegisterException("The email " + sbmEmail + " is not connected yet ");
		}
		response.setSucceeded(true);
		return response;

	}

}
