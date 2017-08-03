package com.tq.clickfunnel.lambda.handler;

import java.util.HashMap;
import java.util.Map;

public enum EventCallback {
    CONTACT_CREATED(EventType.COTACT_CREATED),
    ORDER_CREATED(EventType.ORDER_CREATED),
    ORDER_UPDATED(EventType.ORDER_UPDATED),
    NOT_FOUND ("notfound");

    private String event;
    public static Map<String, EventCallback> mapEventAction = new HashMap<>();
    static {
        mapEventAction.put(EventType.COTACT_CREATED, CONTACT_CREATED);
        mapEventAction.put(EventType.ORDER_CREATED, ORDER_CREATED);
        mapEventAction.put(EventType.ORDER_UPDATED, ORDER_UPDATED);
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
