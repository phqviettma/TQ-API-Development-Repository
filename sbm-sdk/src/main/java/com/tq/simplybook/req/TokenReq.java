package com.tq.simplybook.req;

import java.io.Serializable;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenReq extends LinkedList<String> implements Serializable {
    private static final long serialVersionUID = 2291547240979359788L;
    public TokenReq(String companyLogin, String apiKey) {
        add(companyLogin);
        add(apiKey);
    }
}
