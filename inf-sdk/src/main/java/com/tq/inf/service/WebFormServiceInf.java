package com.tq.inf.service;

import java.util.Map;

import com.tq.inf.exception.InfSDKExecption;

public interface WebFormServiceInf {
    
    public String getHTML(String apiName, String apiKey, Integer formID) throws InfSDKExecption;
    
    //Retrieve Webform IDs {formId=name, }
    public Map<?, ?> getMap(String apiName, String apiKey) throws InfSDKExecption;
}
