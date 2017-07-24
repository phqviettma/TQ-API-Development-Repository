package com.tq.simplybook.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientInfo implements Serializable {
    private static final long serialVersionUID = -5425544867386785418L;
    
    private String id;
    private Client result = null;
    private String jsonrpc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Client getResult() {
        return result;
    }

    public void setResult(Client result) {
        this.result = result;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }
    
    @Override
    public String toString() {
        return "ClientInfo [id=" + id + ", result=" + result + ", jsonrpc=" + jsonrpc + "]";
    }
}
