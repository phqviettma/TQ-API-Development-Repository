package com.tq.clickfunnel.lambda.modle;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CFAffilicateSingupPayload implements Serializable {
    private static final long serialVersionUID = 3471378309216506230L;
    private CFContact contact_profile;
    private String event;
    

    public CFContact getContact_profile() {
        return contact_profile;
    }

    public void setContact_profile(CFContact contact_profile) {
        this.contact_profile = contact_profile;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "CFAffilicateSingupPayload [contact=" + contact_profile + ", event=" + event + "]";
    }
    
    
}
