package com.tq.inf.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.OrderQuery;
import com.tq.inf.service.ActionCallback;
import com.tq.inf.service.ApiContext;
import com.tq.inf.service.OrderServiceInf;
import com.tq.inf.utils.XmlRqcUtils;

public class OrderServiceImpl implements OrderServiceInf {
    @Override
    public Map<?, ?> addOrder(String apiName, String apiKey, OrderQuery orderRecord) throws InfSDKExecption {
        final class PlaceOrderService implements ActionCallback {

            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey()); // secure key

                params.add(orderRecord.getContactID());
                params.add(orderRecord.getCardID()); // credit cart ID ( visa, ..)
                params.add(orderRecord.getPlanID()); // payment plan ID
                params.add(orderRecord.getProductionIDs());
                params.add(orderRecord.getSubscriptionIDs());
                params.add(orderRecord.getProcessSpecials());
                params.add(orderRecord.getPromoCodes());
                params.add(orderRecord.getLeadAffiliateID());
                params.add(orderRecord.getSaleAffiliateID());
                return params;
            }
        }
        return (Map<?, ?>) XmlRqcUtils.execute(apiName, apiKey, new PlaceOrderService(), "DataService.add");
    }

}
