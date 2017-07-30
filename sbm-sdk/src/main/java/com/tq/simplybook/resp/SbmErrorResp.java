package com.tq.simplybook.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SbmErrorResp {
    private SbmError error;
    private Integer id;
    private String jsonrpc = "2.0";

    public SbmError getError() {
        return error;
    }

    public void setError(SbmError error) {
        this.error = error;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    @Override
    public String toString() {
        return "SbmErrorResponse [error=" + error + ", id=" + id + ", jsonrpc=" + jsonrpc + "]";
    }

}
