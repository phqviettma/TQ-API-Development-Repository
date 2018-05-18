package com.tq.googlecalendar.lambda.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tq.common.lambda.dynamodb.dao.GoogleRenewChannelDaoImpl;
import com.tq.common.lambda.dynamodb.impl.GoogleWatchChannelDbServiceImpl;
import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;
import com.tq.common.lambda.dynamodb.service.GoogleCalRenewService;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.req.Params;
import com.tq.googlecalendar.req.StopWatchEventReq;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.req.WatchEventReq;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.resp.WatchEventResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.googlecalendar.utils.GoogleCalendarUtil;

public class GoogleCalChannelRenewHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static final Logger m_log = LoggerFactory.getLogger(GoogleCalChannelRenewHandler.class);
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private static final Integer STATUS_CODE = 200;
	private static final String CHANNEL_TYPE = "web_hook";
	private static final String PARAMS = "3600000";
	private GoogleCalRenewService googleWatchChannelDbService = null;
	private GoogleCalendarApiServiceBuilder apiServiceBuilder = null;
	private TokenGoogleCalendarService tokenCalendarService = null;
	private AmazonDynamoDB amazonDynamoDB = null;
	private Env env = null;

	public GoogleCalChannelRenewHandler() {
		this.env = Env.load();
		this.amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(env.getRegions(), env.getAwsAccessKeyId(),
				env.getAwsSecretAccessKey());
		this.googleWatchChannelDbService = new GoogleWatchChannelDbServiceImpl(
				new GoogleRenewChannelDaoImpl(amazonDynamoDB));
		this.apiServiceBuilder = new GoogleCalendarApiServiceBuilder();
		this.tokenCalendarService = new TokenGoogleCalendarImpl();
	}

	// for testing only
	GoogleCalChannelRenewHandler(GoogleCalendarApiServiceBuilder apiServiceBuilder,
			TokenGoogleCalendarService tokenCalendarService, Env env,
			GoogleCalRenewService googleWatchChannelDbService) {
		this.env = env;
		this.googleWatchChannelDbService = googleWatchChannelDbService;
		this.apiServiceBuilder = apiServiceBuilder;
		this.tokenCalendarService = tokenCalendarService;
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		Long checkDay = buildCheckingTime();
		m_log.info("Start running lambda ");
		List<GoogleRenewChannelInfo> channelInfo = googleWatchChannelDbService.queryItem(checkDay);
		if (channelInfo.size() > 0) {
			for (GoogleRenewChannelInfo item : channelInfo) {
				try {
					TokenReq tokenReq = new TokenReq(env.getGoogleClientId(), env.getGoogleClientSecrets(),
							item.getRefreshToken());
					TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);

					StopWatchEventReq stopEventReq = new StopWatchEventReq(item.getChannelId(), item.getResourceId());
					renewChannelAndCleanUpDb(item, stopEventReq, tokenResp);

				} catch (GoogleApiSDKException e) {
					m_log.info("Internal error", e);
				}

			}
		}
		resp.setBody(buildSuccessBodyResponse());
		resp.setStatusCode(STATUS_CODE);
		return resp;
	}

	private void renewChannelAndCleanUpDb(GoogleRenewChannelInfo channel, StopWatchEventReq stopEventReq,
			TokenResp tokenResp) throws GoogleApiSDKException {
		boolean flag = false;
		long start = System.currentTimeMillis();
		GoogleCalendarApiService calendarApiService = apiServiceBuilder.build(tokenResp.getAccess_token());
		flag = GoogleCalendarUtil.stopWatchChannel(calendarApiService, stopEventReq, flag);
		if (flag) {
			Params params = new Params(PARAMS);
			WatchEventReq watchReq = new WatchEventReq(channel.getChannelId(), CHANNEL_TYPE,
					env.getGoogleNotifyDomain(), params);
			WatchEventResp watchResp = calendarApiService.watchEvent(watchReq, channel.getGoogleEmail());
			m_log.info("Watch channel successfully " + watchResp);
			Long checkingTime = buildCheckingTime(watchResp.getExpiration());
			Long lastQueryTime = buildLastCheckingTime();

			GoogleRenewChannelInfo newChannelInfo = new GoogleRenewChannelInfo(checkingTime, watchResp.getExpiration(),
					channel.getRefreshToken(), channel.getResourceId(), channel.getGoogleEmail(), lastQueryTime,
					channel.getChannelId());
			googleWatchChannelDbService.saveItem(newChannelInfo);
			m_log.info("Save to database successfully " + newChannelInfo);
			googleWatchChannelDbService.deleteItem(channel);
			m_log.info(String.format("renew channel at %d ms", (System.currentTimeMillis() - start)));
		} else {
			m_log.error("Internal error");
		}
	}

	private static Long buildCheckingTime() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy 00:00:00");
			String utcTime = sdf.format(new Date());
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date d;
			d = sdf.parse(utcTime);
			long checkingTime = d.getTime();
			return checkingTime;
		} catch (ParseException e) {
			m_log.info("Parsing error ", e);
		}
		return null;
	}

	private static String buildSuccessBodyResponse() {
		ObjectNode on = jsonMapper.createObjectNode();
		on.put("succeeded", true);
		return on.toString();
	}

	private static Long buildCheckingTime(long expirationTime) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTimeInMillis(expirationTime);
		cal.add(Calendar.DATE, -1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		Date date = cal.getTime();
		long checkingDate = date.getTime();
		return checkingDate;

	}

	private static Long buildLastCheckingTime() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		Date date = cal.getTime();
		long lastQueryTime = date.getTime();
		return lastQueryTime;

	}

}
