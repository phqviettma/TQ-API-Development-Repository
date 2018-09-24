package com.tq.simplybook.resp;

import java.io.Serializable;

public class SbmConfirmBookingResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1022238571531937255L;
	private boolean result;
	private String id;
	private String jsonrpc;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJsonrpc() {
		return jsonrpc;
	}

	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}

	@Override
	public String toString() {
		return "SbmConfirmBookingResponse [result=" + result + ", id=" + id + ", jsonrpc=" + jsonrpc + "]";
	}

}
