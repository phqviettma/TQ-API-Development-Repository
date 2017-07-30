package com.tq.simplybook.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientID implements Serializable {
    private static final long serialVersionUID = 4866377630849191665L;
    private Integer result;
    private String id;

    public ClientID() {
    }

    public int getClientId() {
        return getResult();
    }
    
    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClientID [result=" + result + ", id=" + id + "]";
    }

}
