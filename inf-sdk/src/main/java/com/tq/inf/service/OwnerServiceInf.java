package com.tq.inf.service;

import java.util.List;

import com.tq.inf.exception.InfSDKExecption;

public interface OwnerServiceInf {

    /**
     * 
     * @param apiName
     * @param apiKey
     * @param ownerId
     * @param selectFields
     *            Exception for Country field
     * @return
     * @throws InfSDKExecption
     */
    Object getOwner(String apiName, String apiKey, Integer ownerId, List<?> selectFields) throws InfSDKExecption;
}
