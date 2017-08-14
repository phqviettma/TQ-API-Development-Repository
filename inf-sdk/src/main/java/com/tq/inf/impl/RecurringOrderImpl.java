package com.tq.inf.impl;

import java.util.HashMap;
import java.util.List;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.DataQuery;
import com.tq.inf.service.DataServiceInf;
import com.tq.inf.service.RecurringOrderInf;

public class RecurringOrderImpl implements RecurringOrderInf {

    private DataServiceInf m_dataServiceInf;
    
    public RecurringOrderImpl(DataServiceInf dataServiceInf) {
        m_dataServiceInf = dataServiceInf;
    }

    @SuppressWarnings("serial")
    @Override
    public Object[] getAllRecurringOrder(String apiName, String apiKey, Integer contactId, final List<String> selectedFields) throws InfSDKExecption {
        
        DataQuery dataQuery = new DataQuery()
                .withTable("RecurringOrder")
                .withFilter(new HashMap<Object, Object>(){{
                    put("ContactId", contactId);
                }})
                .withSelectedFields(selectedFields);
        return m_dataServiceInf.query(apiName, apiKey, dataQuery);
    }

    @Override
    @SuppressWarnings("serial")
    public Object[] getRecurringOrderFromOriginatingOrderId(String apiName, String apiKey, Integer contactId, Integer originatingOrderId,
            List<String> selectedFields) throws InfSDKExecption {
        DataQuery dataQuery = new DataQuery()
                .withTable("RecurringOrder")
                .withFilter(new HashMap<Object, Object>(){{
                    put("ContactId", contactId);
                    put("OriginatingOrderId", originatingOrderId);
                }})
                .withSelectedFields(selectedFields);
        return m_dataServiceInf.query(apiName, apiKey, dataQuery);
    }

    @Override
    public Object getLatestRecurringOrderFromProduct(String apiName, String apiKey, Integer contactId, Integer productId,
            List<String> selectedFields) throws InfSDKExecption {
        HashMap<Object, Object> fillter = new  HashMap<>();
        fillter.put("ContactId", contactId);
        fillter.put("ProductId", productId);
        DataQuery dataQuery = new DataQuery()
                .withTable("RecurringOrder")
                .withFilter(fillter)
                .withLimit(1) // just get one record
                .withOrderBy("Id") // increase belong to Id
                .withAscending(Boolean.FALSE)
                .withSelectedFields(selectedFields);
        Object[] query = m_dataServiceInf.query(apiName, apiKey, dataQuery);
        if (query == null || query.length == 0) return null;
        return query[0];
    }

}
