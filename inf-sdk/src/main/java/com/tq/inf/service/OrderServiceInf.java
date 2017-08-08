package com.tq.inf.service;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.OrderQuery;

public interface OrderServiceInf {
    Object addOrder(String apiName, String apiKey, OrderQuery orderRecord) throws InfSDKExecption;
}
