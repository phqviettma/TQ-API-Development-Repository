package com.tq.simplybook.resp;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnitInfoDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 534668280713815659L;
	private String id;
	private List<UnitProviderInfo> result = null;
	private String jsonrpc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<UnitProviderInfo> getResult() {
		return result;
	}

	public void setResult(List<UnitProviderInfo> result) {
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
		return "UnitInfoDetail [id=" + id + ", result=" + result + ", jsonrpc=" + jsonrpc + "]";
	}

}
