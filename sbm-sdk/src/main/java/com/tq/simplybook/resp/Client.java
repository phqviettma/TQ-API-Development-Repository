package com.tq.simplybook.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Client implements Serializable{
    private static final long serialVersionUID = -4178256456913777306L;
    private Integer id;
    private String email;
    private String pass;
    private String name;
    private String phone;
    private String registration_date;
    private Integer is_blocked =0;
    private Integer is_deleted = 0;
    private String email_verification_string = null;
    private String email_verification_date = null;
    private String address1 = null;
    private String address2 = null;
    private String city = null;
    private String zip = null;
    private String country_id = null;
    private String state_id = null;
    private Integer is_confirmed_email = 0;
    private Integer is_confirmed_phone = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public String getEmail_verification_string() {
        return email_verification_string;
    }

    public void setEmail_verification_string(String email_verification_string) {
        this.email_verification_string = email_verification_string;
    }

    public String getEmail_verification_date() {
        return email_verification_date;
    }

    public void setEmail_verification_date(String email_verification_date) {
        this.email_verification_date = email_verification_date;
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

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public Integer getIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(Integer is_blocked) {
        this.is_blocked = is_blocked;
    }

    public Integer getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Integer is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Integer getIs_confirmed_email() {
        return is_confirmed_email;
    }

    public void setIs_confirmed_email(Integer is_confirmed_email) {
        this.is_confirmed_email = is_confirmed_email;
    }

    public Integer getIs_confirmed_phone() {
        return is_confirmed_phone;
    }

    public void setIs_confirmed_phone(Integer is_confirmed_phone) {
        this.is_confirmed_phone = is_confirmed_phone;
    }

	@Override
	public String toString() {
		return "Client [id=" + id + ", email=" + email + ", pass=" + pass + ", name=" + name + ", phone=" + phone
				+ ", registration_date=" + registration_date + ", is_blocked=" + is_blocked + ", is_deleted="
				+ is_deleted + ", email_verification_string=" + email_verification_string + ", email_verification_date="
				+ email_verification_date + ", address1=" + address1 + ", address2=" + address2 + ", city=" + city
				+ ", zip=" + zip + ", country_id=" + country_id + ", state_id=" + state_id + ", is_confirmed_email="
				+ is_confirmed_email + ", is_confirmed_phone=" + is_confirmed_phone + "]";
	}
    
}
