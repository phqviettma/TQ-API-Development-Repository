package com.tq.simplybook.resp;

import java.io.Serializable;

import com.tq.simplybook.req.SpecialDayReq;


public class WorkdayDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3116680200580027716L;
	private String id;
	private SpecialDayReq result = null;
	private String jsonrpc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public SpecialDayReq getResult() {
		return result;
	}

	public void setResult(SpecialDayReq result) {
		this.result = result;
	}

	public String getJsonrpc() {
		return jsonrpc;
	}

	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}

	

	@Override
	public String toString() {
		return "WorkdayInfo [id=" + id + ", result=" + result + ", jsonrpc=" + jsonrpc + "]";
	}

}
