package com.tq.cliniko.lambda.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PractitionersInfo implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 8085674340518789364L;
	private List<Practitioner> practitioners;

	public List<Practitioner> getPractitioners() {
		return practitioners;
	}

	public void setPractitioners(List<Practitioner> practitioners) {
		this.practitioners = practitioners;
	}

	@Override
	public String toString() {
		return "PractitionersInfo [practitioners=" + practitioners + "]";
	}
	

}
