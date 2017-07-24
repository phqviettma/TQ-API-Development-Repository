package com.tq.inf.service;

import java.util.Map;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.OrderQuery;

public interface OrderServiceInf {
    Map<?, ?> addOrder(String apiName, String apiKey, OrderQuery orderRecord) throws InfSDKExecption;
}
