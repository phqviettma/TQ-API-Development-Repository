package com.tq.googlecalendar.lambda.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;
import com.tq.common.lambda.dynamodb.model.SbmBookingInfo;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.GoogleCalRenewService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
import com.tq.common.lambda.dynamodb.service.SbmBookingInfoService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
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
	private static final Logger m_logger = LoggerFactory.getLogger(GoogleDisconnectCalendarHandler.class);
	private Env eVariables = null;
	private GoogleCalendarDbService googleCalendarService = null;
	private TokenGoogleCalendarService tokenCalendarService = new TokenGoogleCalendarImpl();
	private GoogleCalendarApiServiceBuilder apiServiceBuilder = null;
	private GoogleCalRenewService googleCalRenewService = null;
	private GoogleCalendarModifiedSyncService calendarModifiedChannelService = null;
	private SbmSyncFutureBookingsService sbmSyncFutureBookingService = null;
	private SbmListBookingService sbmListBookingService = null;
	private SbmBookingInfoService sbmBookingInfoService = null;

	public GoogleDisconnectCalendarHandler(Env eVariables, GoogleCalendarDbService googleCalendarService,
			TokenGoogleCalendarService tokenCalendarService, GoogleCalendarApiServiceBuilder apiServiceBuilder,
			GoogleCalRenewService googleCalRenewService,
			GoogleCalendarModifiedSyncService calendarModifiedChannelService,
			SbmSyncFutureBookingsService sbmSyncFutureBookingService, SbmListBookingService sbmListBookingService, SbmBookingInfoService sbmBookingInfoService) {
		this.eVariables = eVariables;
		this.googleCalendarService = googleCalendarService;
		this.tokenCalendarService = tokenCalendarService;
		this.apiServiceBuilder = apiServiceBuilder;
		this.googleCalRenewService = googleCalRenewService;
		this.calendarModifiedChannelService = calendarModifiedChannelService;
		this.sbmSyncFutureBookingService = sbmSyncFutureBookingService;
		this.sbmListBookingService = sbmListBookingService;
		this.sbmBookingInfoService = sbmBookingInfoService;
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

				// TSI-59
				TokenResp tokenResp = tokenCalendarService.getTokenIfValidResponse(tokenReq);
				if (tokenResp != null) {
					StopWatchEventReq stopEventReq = new StopWatchEventReq(googleSbm.getChannelId(),
							googleSbm.getGcWatchResourceId());
					GoogleCalendarApiService googleApiService = apiServiceBuilder.build(tokenResp.getAccess_token());
					// work-around: can't stop channel in just one request. So as to make sure the
					// channel is stopped successfully, we try to stop maximum of 3 times .
					GoogleCalendarUtil.stopWatchChannel(googleApiService, stopEventReq);
					if (tokenCalendarService.revokeToken(tokenReq)) {
						m_logger.info("Revoking token " +tokenReq.getRefresh_token() + " successfully");
					} else {
						m_logger.info("Unable to revoke token " +tokenReq.getRefresh_token());
					}
				} else {
					m_logger.info("Could not get token from refresh token " +tokenReq.getRefresh_token());
				}
				stoppedWatch = true;
			}
			if (stoppedWatch) {
				deleteDataWhenPractitionerDisconnect(sbmEmail, googleCalendarSbmSync);
			} else {
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

	private void deleteDataWhenPractitionerDisconnect(String sbmEmail,
			List<GoogleCalendarSbmSync> googleCalendarSbmSync) throws GoogleApiSDKException {
		List<GCModifiedChannel> modifiedChannel = calendarModifiedChannelService.queryEmail(sbmEmail);
		List<GoogleRenewChannelInfo> renewChannel = googleCalRenewService
				.queryEmail(googleCalendarSbmSync.get(0).getGoogleEmail());
		if (!renewChannel.isEmpty() && !modifiedChannel.isEmpty()) {
			googleCalendarService.deleteGoogleItem(googleCalendarSbmSync);
			googleCalRenewService.deleteRenewChannel(renewChannel);
			calendarModifiedChannelService.deleteModifiedItem(modifiedChannel);
			SbmSyncFutureBookings sbmSyncFutureBookings = sbmSyncFutureBookingService
					.load(googleCalendarSbmSync.get(0).getSbmId());
			sbmSyncFutureBookingService.delete(sbmSyncFutureBookings);
			SbmBookingList bookingListItem = sbmListBookingService.load(googleCalendarSbmSync.get(0).getSbmId());
			if (bookingListItem != null) {
				sbmListBookingService.delete(bookingListItem);
			}

		} else {
			throw new GoogleApiSDKException("Internal error");
		}
		 List<SbmBookingInfo> sbmBookingInfo = sbmBookingInfoService.queryEmailIndex(sbmEmail);
		if (!sbmBookingInfo.isEmpty()) {
			sbmBookingInfoService.deleteListBookings(sbmBookingInfo);
		}
	}

}
