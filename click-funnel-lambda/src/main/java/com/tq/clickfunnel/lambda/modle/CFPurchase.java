package com.tq.clickfunnel.lambda.modle;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CFPurchase {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("contact")
    private CFContact contact;

    @JsonProperty("status")
    private String status;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updatedAt")
    private Date updatedAt;

    @JsonProperty("subscription_id")
    private Integer subscriptionId;

    @JsonProperty("charge_id")
    private Integer chargeId;

    @JsonProperty("products")
    private List<CFProducts> products;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CFContact getContact() {
        return contact;
    }

    public void setContact(CFContact contact) {
        this.contact = contact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public List<CFProducts> getProducts() {
        return products;
    }

    public void setProducts(List<CFProducts> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "CFPurchase [id=" + id + ", contact=" + contact + ", status=" + status + ", createdAt=" + createdAt + ", updated_at="
                + updatedAt + ", subscriptionId=" + subscriptionId + ", chargeId=" + chargeId + ", products=" + products + "]";
    }
}
