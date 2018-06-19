package com.tq.cliniko.lambda.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Self implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6865627184731485341L;
	private String self;

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public Self withSelf(String self) {
		this.setSelf(self);
		return this;
	}

	@Override
	public String toString() {
		return "Self [self=" + self + "]";
	}

}
