package com.tq.simplybook.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {

	private String id = null;
	private String title = null;
	private String description;
	private String address1;
	private String address2;
	private String city;
	private String zip;
	private String country_id;
	private String phone;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry_id() {
		return country_id;
	}

	public void setCountry_id(String country_id) {
		this.country_id = country_id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Location() {
	}

	public Location(String id, String title, String description, String address1, String address2, String city,
			String zip, String country_id, String phone) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.zip = zip;
		this.country_id = country_id;
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", title=" + title + ", description=" + description + ", address1=" + address1
				+ ", address2=" + address2 + ", city=" + city + ", zip=" + zip + ", country_id=" + country_id
				+ ", phone=" + phone + "]";
	}

}
