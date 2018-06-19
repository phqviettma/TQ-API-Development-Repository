package com.tq.cliniko.lambda.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Links implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4263174550509907185L;
	private Self links;

	public Self getLinks() {
		return links;
	}

	public void setLinks(Self links) {
		this.links = links;
	}

	public Links withLink(Self links) {
		this.setLinks(links);
		return this;
	}

	@Override
	public String toString() {
		return "Links [links=" + links + "]";
	}

}
