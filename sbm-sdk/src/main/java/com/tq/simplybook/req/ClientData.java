package com.tq.simplybook.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientData implements Serializable {
    
    private static final long serialVersionUID = -7181306280002845610L;
    private String name;
    private String email;
    private String phone;
    private String city;
    private String address1;
    private String address2;
    private String country_id;
    private String zip;
    private String pass = "123456"; // default

    public ClientData() {
    }

    public ClientData(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
    
    @JsonIgnore
    public ClientData withName(String name) {
        this.name = name;
        return this;
    }
    @JsonIgnore
    public ClientData withEmail(String email) {
        this.email = email;
        return this;
    }
    
    @JsonIgnore
    public ClientData withPhone(String phone) {
        this.phone = phone;
        return this;
    }
    
    @JsonIgnore
    public ClientData withCity(String city) {
        this.city = city;
        return this;
    }
    
    @JsonIgnore
    public ClientData withAddress1(String address1) {
        this.address1 = address1;
        return this;
    }
    
    @JsonIgnore
    public ClientData withCountry_id(String country_id) {
        this.country_id = country_id;
        return this;
    }

    public String getPass() {
        return pass;
    }
    
    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "ClientData [name=" + name + ", email=" + email + ", phone=" + phone + ", city=" + city + ", address1=" + address1
                + ", address2=" + address2 + ", country_id=" + country_id + ", zip=" + zip + ", pass=" + pass + "]";
    }
}
