package com.tq.inf.service;

import java.util.Map;

import com.tq.inf.query.DeleteDataQuery;
import com.tq.inf.query.LoadDataQuery;
import com.tq.inf.query.DataQuery;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddDataQuery;
import com.tq.inf.query.CountDataQuery;
import com.tq.inf.query.CustomFieldDataQuery;

public interface DataServiceInf {

    Integer dsAdd(String apiName, String apiKey, final AddDataQuery addQuery) throws InfSDKExecption;
    
    Integer update(String apiName, String apiKey, final AddDataQuery updateQuery) throws InfSDKExecption;

    Boolean delete(String apiName, String apiKey, final DeleteDataQuery deleteQuery) throws InfSDKExecption;

    Object load(String apiName, String apiKey, final LoadDataQuery loadQuery) throws InfSDKExecption;

    Object[] query(String apiName, String apiKey, final DataQuery queryRequest) throws InfSDKExecption;
    
    Integer count(String apiName, String apiKey, final CountDataQuery countQuery) throws InfSDKExecption ;
    
    Integer addCustomField(String apiName, String apiKey, final CustomFieldDataQuery customField) throws InfSDKExecption ;
    
    Boolean updateCustomField(String apiName, String apiKey, Integer customFieldId, Map<?, ?> values) throws InfSDKExecption ;
}
