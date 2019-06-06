package com.tq.simplybook.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditBookResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7977749637061625723L;
	
	private EditBookData result;

	public EditBookData getResult() {
		return result;
	}

	public void setResult(EditBookData result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "EditBookResp [result=" + result.toString() + "]";
	}
}
