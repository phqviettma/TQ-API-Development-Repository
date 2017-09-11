package com.tq.simplybook.resp;

import java.io.Serializable;

import com.tq.simplybook.req.DayInfoReq;

public class WorkTimeDetail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1051498567047837521L;

	private String id;
	
	private Boolean result;
	
	private String jsonrpc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getJsonrpc() {
		return jsonrpc;
	}

	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "WorkTimeDetail [id=" + id + ", result=" + result + ", jsonrpc=" + jsonrpc + "]";
	}

	
	

}
