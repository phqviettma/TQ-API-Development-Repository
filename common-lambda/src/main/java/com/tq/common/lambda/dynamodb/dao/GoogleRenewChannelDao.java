package com.tq.common.lambda.dynamodb.dao;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;
import com.tq.common.lambda.dynamodb.service.GenericItem;

public interface GoogleRenewChannelDao extends GenericItem<GoogleRenewChannelInfo, String> {
	GoogleRenewChannelInfo loadDbItem(Long hashKey, String rangeKey);

	List<GoogleRenewChannelInfo> queryCheckingTime(Long hashkey);

	void saveItem(GoogleRenewChannelInfo channelInfo);

	public List<GoogleRenewChannelInfo> querySbmEmail(String sbmEmail);

	public void deleteChannelList(List<GoogleRenewChannelInfo> googleChannelInfo);
}
