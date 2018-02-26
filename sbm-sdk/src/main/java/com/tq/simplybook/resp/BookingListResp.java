package com.tq.simplybook.resp;

import java.io.Serializable;
import java.util.List;

public class BookingListResp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6329017373965999350L;
	private List<GetBookingResp> result;
	private String id;
	private String jsonrpc;

	public List<GetBookingResp> getResult() {
		return result;
	}

	public void setResult(List<GetBookingResp> result) {
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
		return "BookingListResp [result=" + result + ", id=" + id + ", jsonrpc=" + jsonrpc + "]";
	}

}
