package com.tq.clickfunnel.lambda.modle;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CFContactPayload implements Serializable {
    private static final long serialVersionUID = -1223928206631103206L;
    
    private CFContact contact;
    
    private String event;

    public CFContact getContact() {
        return contact;
    }

    public void setContact(CFContact contact) {
        this.contact = contact;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "CFContactPayload [contact=" + contact + ", event=" + event + "]";
    }
}
