package com.tq.clickfunnel.lambda.modle;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CFContact implements Serializable {
    private static final long serialVersionUID = 1555856465131791499L;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("page_id")
    private Integer pageId;

    @JsonProperty("first_name")
    private String firstName;
    
    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    @JsonProperty("city")
    private String city;

    @JsonProperty("country")
    private String country;

    @JsonProperty("state")
    private String state;

    @JsonProperty("zip")
    private String zip;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("created_at")
    private Date createAt;

    @JsonProperty("updated_at")
    private Date updateAt;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("funnel_id")
    private Integer funnelId;

    @JsonProperty("funnel_step_id")
    private Integer funnelStepId;

    @JsonProperty("cf_uvid")
    private String cfUvid;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_city")
    private String shippingCity;

    @JsonProperty("shipping_country")
    private String shippingCountry;

    @JsonProperty("shipping_state")
    private String shippingState;

    @JsonProperty("shipping_zip")
    private String shippingZip;
    
    @JsonProperty("opt_in_reason")
    private String optInReason;

    public String getOptInReason() {
		return optInReason;
	}

	public void setOptInReason(String optInReason) {
		this.optInReason = optInReason;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getFunnelId() {
        return funnelId;
    }

    public void setFunnelId(Integer funnelId) {
        this.funnelId = funnelId;
    }

    public Integer getFunnelStepId() {
        return funnelStepId;
    }

    public void setFunnelStepId(Integer funnelStepId) {
        this.funnelStepId = funnelStepId;
    }

    public String getCfUvid() {
        return cfUvid;
    }

    public void setCfUvid(String cfUvid) {
        this.cfUvid = cfUvid;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingCountry() {
        return shippingCountry;
    }

    public void setShippingCountry(String shippingCountry) {
        this.shippingCountry = shippingCountry;
    }

    public String getShippingState() {
        return shippingState;
    }

    public void setShippingState(String shippingState) {
        this.shippingState = shippingState;
    }

    public String getShippingZip() {
        return shippingZip;
    }

    public void setShippingZip(String shippingZip) {
        this.shippingZip = shippingZip;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

	@Override
	public String toString() {
		return "CFContact [id=" + id + ", pageId=" + pageId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", name=" + name + ", address=" + address + ", city=" + city + ", country=" + country + ", state="
				+ state + ", zip=" + zip + ", email=" + email + ", phone=" + phone + ", createAt=" + createAt
				+ ", updateAt=" + updateAt + ", ip=" + ip + ", funnelId=" + funnelId + ", funnelStepId=" + funnelStepId
				+ ", cfUvid=" + cfUvid + ", shippingAddress=" + shippingAddress + ", shippingCity=" + shippingCity
				+ ", shippingCountry=" + shippingCountry + ", shippingState=" + shippingState + ", shippingZip="
				+ shippingZip + ", optInReason=" + optInReason + "]";
	}

}
