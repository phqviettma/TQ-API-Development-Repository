package com.tq.simplybook.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DayInfoResp {
	private String id;
    private DayInfo dateInfo = null;
    private String jsonrpc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DayInfo getResult() {
        return dateInfo;
    }

    public void setResult(DayInfo result) {
        this.dateInfo = result;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

	@Override
	public String toString() {
		return "DayInfoResp [id=" + id + ", dateInfo=" + dateInfo + ", jsonrpc=" + jsonrpc + "]";
	}
    
}
