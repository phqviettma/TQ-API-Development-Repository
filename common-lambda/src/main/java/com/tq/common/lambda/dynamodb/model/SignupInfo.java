package com.tq.common.lambda.dynamodb.model;

public class SignupInfo {
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
    
    public SignupInfo withContactId(Integer contactId) {
        this.contactId = contactId;
        return this;
    }

    public SignupInfo withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public SignupInfo withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public SignupInfo withEmail(String email) {
        this.email = email;
        return this;
    }

    public SignupInfo withPhone1(String phone1) {
        this.phone1 = phone1;
        return this;
    }

    public SignupInfo withCountry(String country) {
        this.country = country;
        return this;
    }

    public SignupInfo withAddress1(String address1) {
        this.address1 = address1;
        return this;
    }

    public SignupInfo withCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    public SignupInfo withUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}   
