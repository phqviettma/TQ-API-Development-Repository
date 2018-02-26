package com.tq.cliniko.lambda.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tq.cliniko.lambda.model.Links;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Practitioner implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2288684148660202490L;
	private Integer id;
	private Links user;
	private String display_name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public Links getUser() {
		return user;
	}

	public void setUser(Links user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Practitioner [id=" + id + ", user=" + user + ", display_name=" + display_name + "]";
	}

}
