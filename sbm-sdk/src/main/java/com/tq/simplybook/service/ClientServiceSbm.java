package com.tq.simplybook.service;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.ClientData;
import com.tq.simplybook.resp.Client;

/**
 * https://user-api.simplybook.me/admin/
 * @author phqviet
 *
 */
public interface ClientServiceSbm {

    Integer addClient(String companyLogin, String endpointAdmin, String userToken, ClientData client) throws SbmSDKException;
    
    Integer editClient(String companyLogin, String endpointAdmin, String userToken,  Integer clientId, ClientData clientData) throws SbmSDKException;
    
    Client getClient(String companyLogin, String endpointAdmin, String userToken, Integer clientID) throws SbmSDKException ;
    
}
