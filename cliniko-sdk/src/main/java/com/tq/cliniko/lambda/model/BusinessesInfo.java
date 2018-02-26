package com.tq.cliniko.lambda.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessesInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3884621047712516986L;
	private List<Businesses> businesses;

	public List<Businesses> getBusinesses() {
		return businesses;
	}

	public void setBusinesses(List<Businesses> businesses) {
		this.businesses = businesses;
	}

	@Override
	public String toString() {
		return "BusinessesInfo [businesses=" + businesses + "]";
	}

}
