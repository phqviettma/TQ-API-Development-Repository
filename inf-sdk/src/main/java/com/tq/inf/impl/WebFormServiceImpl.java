package com.tq.inf.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.service.ActionCallback;
import com.tq.inf.service.ApiContext;
import com.tq.inf.service.WebFormServiceInf;
import com.tq.inf.utils.XmlRqcUtils;

public class WebFormServiceImpl implements WebFormServiceInf {
    @Override
    public String getHTML(String apiName, String apiKey, final Integer formID) throws InfSDKExecption {
        return (String) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {

            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey()); // secure key
                
                params.add(formID);
                return params;
            }
        }, "WebFormService.getHTML");
    }

    @Override
    public Map<?, ?> getMap(String apiName, String apiKey) throws InfSDKExecption {
        return (Map<?, ?>) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {

            @Override
            public List<?> getParameters(ApiContext apiContext) {
                return Arrays.asList(apiContext.getApiKey());// secure key
            }
        }, "WebFormService.getMap");
    }
}
