package com.tq.simplybook.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddClientReq implements Serializable {
    private static final long serialVersionUID = -4600818144481967759L;

    private ClientData clientData;

    public AddClientReq() {
    }

    public AddClientReq(ClientData clientData) {
        this.clientData = clientData;
    }

    public ClientData getClientData() {
        return clientData;
    }

    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }

    @Override
    public String toString() {
        return "ClientReq [clientData=" + clientData + "]";
    }
}
