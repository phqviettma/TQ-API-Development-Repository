package com.tq.clickfunnel.lambda.handler;

import java.util.HashMap;
import java.util.Map;

public enum EventCallback {
    CONTACT_CREATED(EventType.COTACT_CREATED),
    AFFILICATE_SIGNUP(EventType.AFFILICATE_SIGNUP),
    ORDER_CREATED(EventType.ORDER_CREATED),
    ORDER_UPDATED(EventType.ORDER_UPDATED),
    ORDER_DELETED(EventType.ORDER_DELETED),
    NOT_FOUND ("notfound");

    private String event;
    public static Map<String, EventCallback> mapEventAction = new HashMap<>();
    static {
        mapEventAction.put(EventType.COTACT_CREATED, CONTACT_CREATED);
        mapEventAction.put(EventType.AFFILICATE_SIGNUP, AFFILICATE_SIGNUP);
        mapEventAction.put(EventType.ORDER_CREATED, ORDER_CREATED);
        mapEventAction.put(EventType.ORDER_UPDATED, ORDER_UPDATED);
        mapEventAction.put(EventType.ORDER_DELETED, ORDER_DELETED);
    }

    EventCallback(String event) {
        this.setEvent(event);
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public static EventCallback on(String event) {
        return mapEventAction.get(event) == null ? null : mapEventAction.get(event);
    }
}
