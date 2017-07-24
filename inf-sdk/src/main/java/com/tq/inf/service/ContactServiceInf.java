package com.tq.inf.service;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddNewContactQuery;
import com.tq.inf.query.AddDataQuery;
import com.tq.inf.query.ApplyTagQuery;
import com.tq.inf.query.LoadContactQuery;
import com.tq.inf.query.SearchEmailQuery;

public interface ContactServiceInf {
    
    Integer add(String apiName, String apiKey, AddNewContactQuery addQuery) throws InfSDKExecption;

    Integer addWithDupCheck(String apiName, String apiKey, AddNewContactQuery addQuery) throws InfSDKExecption;

    Object load(String apiName, String apiKey,  LoadContactQuery loaderQuery) throws InfSDKExecption;

    Object[] findByEmail(String apiName, String apiKey,  SearchEmailQuery emailQuery) throws InfSDKExecption;

    Integer update(String apiName, String apiKey, AddDataQuery updateQuery) throws InfSDKExecption;

    Boolean addToGroup(String apiName, String apiKey, ApplyTagQuery applyTagQuery) throws InfSDKExecption;

    Boolean removeTag(String apiName, String apiKey, ApplyTagQuery removeTagQuery) throws InfSDKExecption;

    Boolean appyTag(String apiName, String apiKey, ApplyTagQuery applyTagQuery) throws InfSDKExecption;
    
    Boolean merge(String apiName, String apiKey, Integer duplicateContactId) throws InfSDKExecption;
}
