package com.tq.sbmsync.lambda.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SbmSyncReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 208996480378161311L;
	private String agent;
	private SbmSyncReqParams params;

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public SbmSyncReqParams getParams() {
		return params;
	}

	public void setParams(SbmSyncReqParams params) {
		this.params = params;
	}

	public SbmSyncReq(String agent, SbmSyncReqParams params) {

		this.agent = agent;
		this.params = params;
	}

	public SbmSyncReq() {

	}

	@Override
	public String toString() {
		return "SbmSyncReq [agent=" + agent + ", params=" + params + "]";
	}

}
