package com.tq.calendar.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StopWatchEventReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3198474152056742295L;
	private String id;
	private String resourceId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	@Override
	public String toString() {
		return "StopWatchEventReq [id=" + id + ", resourceId=" + resourceId + "]";
	}

	public StopWatchEventReq(String id, String resourceId) {
		this.id = id;
		this.resourceId = resourceId;
	}

	public StopWatchEventReq() {

	}

}
