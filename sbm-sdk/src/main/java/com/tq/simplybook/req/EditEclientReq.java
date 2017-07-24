package com.tq.simplybook.req;

import java.io.Serializable;

public class EditEclientReq implements Serializable {
    private static final long serialVersionUID = 2349330825336163271L;
    private Integer clientId;
    private ClientData clientData;

    public EditEclientReq(Integer clientId, ClientData clientData) {
        this.clientId = clientId;
        this.clientData = clientData;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public ClientData getClientData() {
        return clientData;
    }

    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }

    @Override
    public String toString() {
        return "EditEclientReq [clientId=" + clientId + ", clientData=" + clientData + "]";
    }
}
