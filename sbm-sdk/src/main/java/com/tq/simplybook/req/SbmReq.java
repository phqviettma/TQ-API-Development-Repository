package com.tq.simplybook.req;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SbmReq<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1049888275931534942L;
    private static final AtomicLong count = new AtomicLong(1);
    
    private String jsonrpc = "2.0";
    private String method;
    private String id = String.valueOf(count.getAndIncrement());
    private T params;
    
    public SbmReq(String method, T params) {
        this.method = method;
        this.setParams(params);
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "SbmReq [jsonrpc=" + jsonrpc + ", method=" + method + ", id=" + id + ", params=" + params + "]";
    }
}
