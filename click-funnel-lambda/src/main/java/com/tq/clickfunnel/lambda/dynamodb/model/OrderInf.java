package com.tq.clickfunnel.lambda.dynamodb.model;

public class OrderInf {
    
    private Integer contactId;
    
    private String email;
    
    private Integer orderId;
    
    private Integer invoiceId;
    
    private Integer productId;
    
    private String createdAt;
    
    private String updatedAt;
    
    public Integer getContactId() {
        return contactId;
    }
    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Integer getOrderId() {
        return orderId;
    }
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    public Integer getInvoiceId() {
        return invoiceId;
    }
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }
    public Integer getProductId() {
        return productId;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
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
    @Override
    public String toString() {
        return "OrderInf [contactId=" + contactId + ", email=" + email + ", orderId=" + orderId + ", invoiceId=" + invoiceId
                + ", productId=" + productId + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}
