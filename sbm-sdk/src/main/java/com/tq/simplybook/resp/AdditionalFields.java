package com.tq.simplybook.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdditionalFields implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5542826356193810311L;
	private String field_name;
	private String field_title;
	private String field_position;
	private String field_type;
	private String field_id;
	private String value;

	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public String getField_title() {
		return field_title;
	}

	public void setField_title(String field_title) {
		this.field_title = field_title;
	}

	public String getField_position() {
		return field_position;
	}

	public void setField_position(String field_position) {
		this.field_position = field_position;
	}

	public String getField_type() {
		return field_type;
	}

	public void setField_type(String field_type) {
		this.field_type = field_type;
	}

	public String getField_id() {
		return field_id;
	}

	public void setField_id(String field_id) {
		this.field_id = field_id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "AdditionalFields [field_name=" + field_name + ", field_title=" + field_title + ", field_position="
				+ field_position + ", field_type=" + field_type + ", field_id=" + field_id + ", value=" + value + "]";
	}

}
