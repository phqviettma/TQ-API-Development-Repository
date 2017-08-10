package com.tq.clickfunnel.lambda.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeletedOrderResp {

    private Integer contactId;

    private Integer invoiceId;

    private Integer subscriptionId;

    private Integer purchaseId;

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }
    
    public DeletedOrderResp withContactId(Integer contactId) {
        this.contactId = contactId;
        return this;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }
    
    public DeletedOrderResp withInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
    
    public DeletedOrderResp withSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }


    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }
    
    public DeletedOrderResp withPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
        return this;
    }

    @Override
    public String toString() {
        return "DeletedOrderResp [contactId=" + contactId + ", invoiceId=" + invoiceId + ", subscriptionId=" + subscriptionId
                + ", purchaseId=" + purchaseId + "]";
    }
}
