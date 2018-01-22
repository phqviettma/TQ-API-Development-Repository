package com.tq.calendar.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.calendar.exception.GoogleApiSDKException;
import com.tq.calendar.req.GoogleCalendarParser;
import com.tq.calendar.req.TokenReq;
import com.tq.calendar.req.UtilsExecutor;
import com.tq.calendar.resp.TokenResp;
import com.tq.calendar.service.TokenGoogleCalendarService;

public class TokenGoogleCalendarImpl implements TokenGoogleCalendarService{
	private static final Logger m_log = LoggerFactory.getLogger(TokenGoogleCalendarImpl.class);
	@Override
	public TokenResp getToken(TokenReq req) throws GoogleApiSDKException {
		String jsonResp;
		try {
			jsonResp = tokenRequest(req);
			m_log.info("Json token response" + String.valueOf(jsonResp));
			return GoogleCalendarParser.readJsonValueForObject(jsonResp, TokenResp.class);
		} catch (Exception e) {
			throw new GoogleApiSDKException(e);
		}
	}
	private String tokenRequest(TokenReq body) throws Exception {
		HttpPost req = new HttpPost();
		req.setURI(URI.create("https://www.googleapis.com/oauth2/v4/token"));
		req.setHeader("Content-Type", "application/x-www-form-urlencoded");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("client_id", body.getClient_id()));
		params.add(new BasicNameValuePair("client_secret", body.getClient_secret()));
		params.add(new BasicNameValuePair("refresh_token", body.getRefresh_token()));
		params.add(new BasicNameValuePair("grant_type", body.getGrant_type()));
		req.setEntity(new UrlEncodedFormEntity(params));
		return UtilsExecutor.request(req);
	}
}
