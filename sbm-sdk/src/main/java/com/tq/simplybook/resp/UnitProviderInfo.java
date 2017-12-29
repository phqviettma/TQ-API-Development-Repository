package com.tq.simplybook.resp;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnitProviderInfo implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = -1585596480349139026L;
	private String id;
	private String name;
	private String description;
	private String phone;
	private String email;
	private Map<String, Object> event_map;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<String, Object> getEvent_map() {
		return event_map;
	}

	public void setEvent_map(Map<String, Object> event_map) {
		this.event_map = event_map;
	}

	@Override
	public String toString() {
		return "UnitProviderInfo [id=" + id + ", name=" + name + ", description=" + description + ", phone=" + phone
				+ ", email=" + email + ", event_map=" + event_map + "]";
	}

}
