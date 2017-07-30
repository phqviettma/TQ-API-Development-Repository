package com.tq.clickfunnel.lambda.modle;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactPayload implements Serializable {
    private static final long serialVersionUID = -1223928206631103206L;
    
    private Contact contact;
    
    private String event;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
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
        return "ContactPayload [contact=" + contact + ", event=" + event + "]";
    }
}
