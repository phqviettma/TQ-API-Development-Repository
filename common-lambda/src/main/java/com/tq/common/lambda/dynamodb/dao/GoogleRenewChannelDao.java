package com.tq.common.lambda.dynamodb.dao;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;
import com.tq.common.lambda.dynamodb.service.GenericItem;

public interface GoogleRenewChannelDao extends GenericItem<GoogleRenewChannelInfo, Long> {
	GoogleRenewChannelInfo loadDbItem(Long hashKey, String rangeKey);

	List<GoogleRenewChannelInfo> queryItem(Long hashkey);
	
	void saveItem(GoogleRenewChannelInfo channelInfo);
}
