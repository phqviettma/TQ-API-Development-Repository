package com.tq.common.lambda.dynamodb.service;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;

public interface GoogleCalRenewService extends BaseItemService<GoogleRenewChannelInfo, String> {
	GoogleRenewChannelInfo loadDbItem(Long hashKey, String rangeKey);

	List<GoogleRenewChannelInfo> queryCheckingTime(Long hashkey);

	public void saveItem(GoogleRenewChannelInfo channel);

	public void deleteItem(GoogleRenewChannelInfo channel);

	public List<GoogleRenewChannelInfo> queryEmail(String sbmEmail);

	public void deleteRenewChannel(List<GoogleRenewChannelInfo> channelInfo);
}
