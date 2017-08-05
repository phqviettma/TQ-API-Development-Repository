package com.tq.simplybook.utils;

import java.io.IOException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.SbmError;
import com.tq.simplybook.resp.SbmErrorResp;

public final class SbmUtils {
    private static ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(SbmUtils.class);

    public static <T extends Serializable> String invokeTokenSignIn(String method, String endpoint, T req) throws SbmSDKException {
        String jsonResp = null;
        try {
            jsonResp = SbmExecute.executeWithNoneToken(endpoint, method, req);
        } catch (Exception e) {
            throw new SbmSDKException(method, e);
        }
        return jsonResp;
    }

    public static <T extends Serializable> String invokeUserTokenSignIn(String endpointLogin, String method, T req) throws SbmSDKException {
        String jsonResp = null;
        try {
            jsonResp = SbmExecute.executeWithNoneToken(endpointLogin, method, req);
        } catch (Exception e) {
            throw new SbmSDKException(method, e);
        }
        return jsonResp;
    }

    public static <T> T readValueForObject(String jsonResp, Class<T> classes) throws SbmSDKException {
        T token = null;
        try {
            /**
             * if We wan to handle the error during parsing json response, the response class does not mark
             * 
             * @JsonIgnoreProperties(ignoreUnknown = true) such as getToken
             */
            token = JSON_MAPPER.readValue(jsonResp, classes);
        } catch (IOException e) {
            handleParseJsonError(jsonResp, e);
        }
        return token;
    }

    private static void handleParseJsonError(String jsonResp, Exception e) throws SbmSDKException {
        try {
            SbmErrorResp resp = JSON_MAPPER.readValue(jsonResp, SbmErrorResp.class);
            SbmError error = resp.getError();
            throw new SbmSDKException(error.getCode() + ":" + error.getMessage(), e);
        } catch (IOException e1) {
            log.error("{}", e1.getMessage());
            throw new SbmSDKException("Error during reading response error : " + jsonResp, e1);
        }
    }

}
