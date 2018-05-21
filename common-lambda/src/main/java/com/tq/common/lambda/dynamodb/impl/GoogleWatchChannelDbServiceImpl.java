package com.tq.common.lambda.dynamodb.impl;

import java.util.List;

import com.tq.common.lambda.dynamodb.dao.GoogleRenewChannelDao;
import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;
import com.tq.common.lambda.dynamodb.service.GoogleCalRenewService;

public class GoogleWatchChannelDbServiceImpl implements GoogleCalRenewService {
	private GoogleRenewChannelDao googleWatchChannelDao;

	public GoogleWatchChannelDbServiceImpl(GoogleRenewChannelDao googleWatchChannelDao) {
		this.googleWatchChannelDao = googleWatchChannelDao;
	}

	@Override
	public void put(GoogleRenewChannelInfo item) {
		googleWatchChannelDao.putItem(item);

	}

	@Override
	public GoogleRenewChannelInfo load(String key) {

		return googleWatchChannelDao.loadItem(key);
	}

	@Override
	public GoogleRenewChannelInfo loadDbItem(Long hashKey, String rangeKey) {
		return googleWatchChannelDao.loadDbItem(hashKey, rangeKey);
	}

	@Override
	public List<GoogleRenewChannelInfo> queryCheckingTime(Long hashkey) {

		return googleWatchChannelDao.queryCheckingTime(hashkey);
	}

	@Override
	public void saveItem(GoogleRenewChannelInfo channelInfo) {
		googleWatchChannelDao.saveItem(channelInfo);
	}

	@Override
	public void deleteItem(GoogleRenewChannelInfo channel) {
		googleWatchChannelDao.deleteItem(channel);

	}

	@Override
	public List<GoogleRenewChannelInfo> queryEmail(String sbmEmail) {

		return googleWatchChannelDao.querySbmEmail(sbmEmail);
	}

	@Override
	public void deleteRenewChannel(List<GoogleRenewChannelInfo> channelInfos) {
		googleWatchChannelDao.deleteChannelList(channelInfos);

	}

}
