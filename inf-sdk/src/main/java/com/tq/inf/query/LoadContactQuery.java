package com.tq.inf.query;

import java.util.List;

public class LoadContactQuery {
	private Integer contactID;
	private List<String> fields;

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public LoadContactQuery withSelectedFields(List<String> selectedFields) {
		this.fields = selectedFields;
		return this;
	}

	public Integer getContactID() {
		return contactID;
	}

	public void setContactID(Integer contactID) {
		this.contactID = contactID;
	}

	@Override
	public String toString() {
		return "LoadContactQuery [contactID=" + contactID + ", fields=" + fields + "]";
	}

}
