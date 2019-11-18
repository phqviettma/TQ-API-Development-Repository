package com.tq.simplybook.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3013186101490711494L;

	@JsonProperty("unit_group_id")
	private String providerId;
	
	@JsonProperty("event_id")
	private String eventId;

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	@Override
	public String toString() {
		return "ProviderInfo [providerId=" + providerId + ", eventId=" + eventId + "]";
	}
}
