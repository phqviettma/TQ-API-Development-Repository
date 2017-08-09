package com.tq.common.lambda.dynamodb.model;

import java.io.Serializable;

public class ClientInfo implements Serializable{
    private static final long serialVersionUID = -5851343590430915427L;

    /**
     * Client ID of click funnel
     */
    private Integer clientId;

    /**
     * Contact ID of Infusionsoft
     */
    private Integer contactId;

    private String firstName;

    private String lastName;

    private String email;

    private String phone1;

    private String country;

    private String city;

    private String zip;

    private String address1;

    private String address2;
    
    private String createdAt;
    
    private String updatedAt;

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
    
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ClientInfo withClientId(Integer clientId) {
        this.clientId = clientId;
        return this;
    }

    public ClientInfo withContactId(Integer contactId) {
        this.contactId = contactId;
        return this;
    }

    public ClientInfo withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ClientInfo withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ClientInfo withEmail(String email) {
        this.email = email;
        return this;
    }

    public ClientInfo withPhone1(String phone1) {
        this.phone1 = phone1;
        return this;
    }

    public ClientInfo withCountry(String country) {
        this.country = country;
        return this;
    }

    public ClientInfo withAddress1(String address1) {
        this.address1 = address1;
        return this;
    }

    public ClientInfo withCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    public ClientInfo withUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }


    @Override
    public String toString() {
        return "ClientInfo [clientId=" + clientId + ", contactId=" + contactId + ", firstName=" + firstName + ", lastName=" + lastName
                + ", email=" + email + ", phone1=" + phone1 + ", country=" + country + ", city=" + city + ", zip=" + zip + ", address1="
                + address1 + ", address2=" + address2 + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}
