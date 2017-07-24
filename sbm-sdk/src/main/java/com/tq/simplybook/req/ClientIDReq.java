package com.tq.simplybook.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientIDReq implements Serializable {
    private static final long serialVersionUID = 8558013102817471206L;

    public ClientIDReq() {
    }

    public ClientIDReq(Integer clientId) {
        this.clientId = clientId;
    }

    private Integer clientId;

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
}
