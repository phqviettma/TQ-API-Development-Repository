package com.tq.common.lambda.dynamodb.service;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;

public interface GoogleCalRenewService extends BaseItemService<GoogleRenewChannelInfo, Long> {
	GoogleRenewChannelInfo loadDbItem(Long hashKey, String rangeKey);

	List<GoogleRenewChannelInfo> queryItem(Long hashkey);

	public void saveItem(GoogleRenewChannelInfo channel);

	public void deleteItem(GoogleRenewChannelInfo channel);

	public GoogleRenewChannelInfo query(String channelId);
}
