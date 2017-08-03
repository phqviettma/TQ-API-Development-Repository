package com.tq.clickfunnel.lambda.modle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CFOrderPayload {
    
    @JsonProperty("event")
    private String event;
    
    @JsonProperty("purchase")
    private CFPurchase purchase;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public CFPurchase getPurchase() {
        return purchase;
    }

    public void setPurchase(CFPurchase purchase) {
        this.purchase = purchase;
    }

    @Override
    public String toString() {
        return "CFOrderPayload [event=" + event + ", purchase=" + purchase + "]";
    }
}
