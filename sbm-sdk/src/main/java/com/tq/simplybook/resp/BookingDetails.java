package com.tq.simplybook.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDetails implements Serializable {
	private static final long serialVersionUID = 2145386820679362077L;
	
	private String id;
    private BookingInfo result = null;
    private String jsonrpc;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BookingInfo getResult() {
		return result;
	}
	public void setResult(BookingInfo result) {
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
		return "BookingDetails [id=" + id + ", result=" + result + ", jsonrpc=" + jsonrpc + "]";
	}
    
}
