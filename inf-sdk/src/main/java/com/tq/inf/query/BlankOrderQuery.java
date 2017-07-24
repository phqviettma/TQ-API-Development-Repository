package com.tq.inf.query;

import java.util.Date;

public class BlankOrderQuery {

    private Integer contactID;
    private String description;
    private Date orderDate; //// iso8601
    private Integer leadAffiliateId;
    private Integer saleAffiliateId;

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getLeadAffiliateId() {
        return leadAffiliateId;
    }

    public void setLeadAffiliateId(Integer leadAffiliateId) {
        this.leadAffiliateId = leadAffiliateId;
    }

    public Integer getSaleAffiliateId() {
        return saleAffiliateId;
    }

    public void setSaleAffiliateId(Integer saleAffiliateId) {
        this.saleAffiliateId = saleAffiliateId;
    }

    public BlankOrderQuery withContactID(Integer contactID) {
        this.contactID = contactID;
        return this;
    }

    public BlankOrderQuery withDescription(String description) {
        this.description = description;
        return this;
    }

    public BlankOrderQuery withOrderDate(Date orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public BlankOrderQuery withLeadAffiliateId(Integer leadAffiliateId) {
        this.leadAffiliateId = leadAffiliateId;
        return this;
    }

    public BlankOrderQuery withSaleAffiliateId(Integer saleAffiliateId) {
        this.saleAffiliateId = saleAffiliateId;
        return this;
    }

    @Override
    public String toString() {
        return "BlankOrderQuery [contactID=" + contactID + ", description=" + description + ", orderDate=" + orderDate
                + ", leadAffiliateId=" + leadAffiliateId + ", saleAffiliateId=" + saleAffiliateId + "]";
    }
}
