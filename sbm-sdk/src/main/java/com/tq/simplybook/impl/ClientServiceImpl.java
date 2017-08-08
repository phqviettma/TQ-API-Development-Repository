package com.tq.simplybook.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.ClientIDReq;
import com.tq.simplybook.req.AddClientReq;
import com.tq.simplybook.req.ClientData;
import com.tq.simplybook.req.EditEclientReq;
import com.tq.simplybook.resp.Client;
import com.tq.simplybook.resp.ClientID;
import com.tq.simplybook.resp.ClientInfo;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.utils.SbmExecute;
import com.tq.simplybook.utils.SbmUtils;

public class ClientServiceImpl implements ClientServiceSbm {

    public static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Override
    public Integer addClient(String companyLogin, String endpointAdmin, String userToken,  ClientData clientData) throws SbmSDKException {
        try {
            String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpointAdmin, userToken, "addClient", new AddClientReq(clientData));
            return SbmUtils.readValueForObject(jsonResp, ClientID.class).getClientId();
        } catch (Exception e) {
            throw new SbmSDKException(e.getMessage() +" during addClient()", e);
        }
    }
    
    public Integer editClient(String companyLogin, String endpointAdmin, String userToken, Integer clientId,  ClientData clientData)
            throws SbmSDKException {
        try {
            String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpointAdmin, userToken, "editClient", new EditEclientReq(clientId, clientData));
            return SbmUtils.readValueForObject(jsonResp, ClientID.class).getClientId();
        } catch (Exception e) {
            throw new SbmSDKException(e.getMessage() +" during editClient()", e);
        }
    }

    @Override
    public Client getClient(String companyLogin, String endpointAdmin, String userToken, Integer clientID) throws SbmSDKException {
        try {
            String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpointAdmin, userToken, "getClient",
                    new ClientIDReq(clientID));
            ClientInfo readValueForObject = SbmUtils.readValueForObject(jsonResp, ClientInfo.class);
            return readValueForObject != null ? readValueForObject.getResult() : null;
        } catch (Exception e) {
            throw new SbmSDKException(e.getMessage() +" during getClient()", e);
        }
    }

}
