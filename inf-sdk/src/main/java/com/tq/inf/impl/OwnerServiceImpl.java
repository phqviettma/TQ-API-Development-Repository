package com.tq.inf.impl;

import java.util.List;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.LoadDataQuery;
import com.tq.inf.service.OwnerServiceInf;

public class OwnerServiceImpl extends DataServiceImpl implements OwnerServiceInf {

    @Override
    public Object getOwner(String apiName, String apiKey, final Integer ownerId, final List<?> selectedFields) throws InfSDKExecption {
        return load(apiName, apiKey, new LoadDataQuery().
                            withRecordID(ownerId).withSelectedFields(selectedFields).withTable("User"));
    }
}
