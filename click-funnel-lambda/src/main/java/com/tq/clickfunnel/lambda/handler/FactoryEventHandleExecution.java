package com.tq.clickfunnel.lambda.handler;

import java.util.HashMap;
import java.util.Map;

public class FactoryEventHandleExecution {
    private Map<EventCallback, EventPayloadExecution> m_eventExecution = new HashMap<>();

    public FactoryEventHandleExecution() {
        // registration for Event Contact
        regis(EventCallback.CONTACT_CREATED, new HandleEventContactExecution());
        
        regis(EventCallback.AFFILICATE_SIGNUP, new HandleEventAffiliateBackpackSignupExecution());
        
        // registration for Event Order/Payment
        regis(EventCallback.ORDER_CREATED, new HandleEventCreatedOrderExecution());
        regis(EventCallback.ORDER_UPDATED, new HandleEventUpdatedOrderExecution());
        regis(EventCallback.ORDER_DELETED, new HandleEventDeletedOrderExecution());
        
        // registration for not existed event
        regis(EventCallback.NOT_FOUND, new HandleNotFoundEventExecution());
    }

    public static FactoryEventHandleExecution standards() {
        return new FactoryEventHandleExecution();
    }

    public EventPayloadExecution ofExecution(EventCallback eventCallback) {
        return m_eventExecution.get(eventCallback);
    }

    public EventPayloadExecution getHandleEvent(EventCallback eventCallback) {
        return m_eventExecution.get(eventCallback) == null 
                ? m_eventExecution.get(EventCallback.NOT_FOUND)
                : m_eventExecution.get(eventCallback);
    }

    public EventPayloadExecution getHandleEvent(String eventCallback) {
        return getHandleEvent(EventCallback.on(eventCallback));
    }

    private void regis(EventCallback event, EventPayloadExecution execution) {
        m_eventExecution.put(event, execution);
    }
}
