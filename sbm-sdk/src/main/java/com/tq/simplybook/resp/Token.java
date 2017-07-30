package com.tq.simplybook.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Token implements Serializable {
    private static final long serialVersionUID = -6270014247092481560L;

    private String result;
    private String id;
    private String jsonrpc;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getTokenID() {
        return getResult();
    }

    @Override
    public String toString() {
        return "Token [result=" + result + ", id=" + id + ", jsonrpc=" + jsonrpc + "]";
    }
}
