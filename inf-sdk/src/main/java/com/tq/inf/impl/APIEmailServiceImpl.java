package com.tq.inf.impl;

import java.util.LinkedList;
import java.util.List;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.OptQuery;
import com.tq.inf.service.APIEmailServiceInf;
import com.tq.inf.service.ActionCallback;
import com.tq.inf.service.ApiContext;
import com.tq.inf.utils.XmlRqcUtils;

public class APIEmailServiceImpl implements APIEmailServiceInf {
    @Override
    public Boolean optIn(String apiName, String apiKey, final OptQuery optInQuery) throws InfSDKExecption {
        return (Boolean) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {

            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey());

                params.add(optInQuery.getEmail());
                params.add(optInQuery.getReasion());
                return params;
            }
        }, "APIEmailService.optIn");
    }

    @Override
    public Boolean optOut(String apiName, String apiKey, final OptQuery optOutQuery) throws InfSDKExecption {
        return (Boolean) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {

            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey());

                params.add(optOutQuery.getEmail());
                params.add(optOutQuery.getReasion());
                return params;
            }
        }, "APIEmailService.optOut");
    }

    @Override
    public Boolean getOptStatus(String apiName, String apiKey, final String email) throws InfSDKExecption {
        return (Boolean) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {

            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey());

                params.add(email);
                return params;
            }
        }, "APIEmailService.getOptStatus");
    }
}
