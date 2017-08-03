package com.tq.clickfunnel.lambda.modle;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CFProducts {
    
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("amount")
    private CFAmount amount;
    
    @JsonProperty("amount_currency")
    private String amountCurrency;
    
    @JsonProperty("created_at")
    private Date createdAt;
    
    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("billing_integration")
    private String billingIntegration;
    
    @JsonProperty("infusionsoft_product_id")
    private Integer infusionsoftProduct_id;
    
    @JsonProperty("subject")
    private String subject;
    
    @JsonProperty("thank_you_page_id")
    private Integer thankYouPageId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmountCurrency() {
        return amountCurrency;
    }

    public void setAmountCurrency(String amountCurrency) {
        this.amountCurrency = amountCurrency;
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

    public String getBillingIntegration() {
        return billingIntegration;
    }

    public void setBillingIntegration(String billingIntegration) {
        this.billingIntegration = billingIntegration;
    }

    public Integer getInfusionsoftProduct_id() {
        return infusionsoftProduct_id;
    }

    public void setInfusionsoftProduct_id(Integer infusionsoftProduct_id) {
        this.infusionsoftProduct_id = infusionsoftProduct_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getThankYouPageId() {
        return thankYouPageId;
    }

    public void setThankYouPageId(Integer thankYouPageId) {
        this.thankYouPageId = thankYouPageId;
    }

    public CFAmount getAmount() {
        return amount;
    }

    public void setAmount(CFAmount amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "CFProducts [id=" + id + ", name=" + name + ", amount=" + amount + ", amountCurrency=" + amountCurrency + ", createdAt="
                + createdAt + ", updatedAt=" + updatedAt + ", billingIntegration=" + billingIntegration + ", infusionsoftProduct_id="
                + infusionsoftProduct_id + ", subject=" + subject + ", thankYouPageId=" + thankYouPageId + "]";
    }
}
