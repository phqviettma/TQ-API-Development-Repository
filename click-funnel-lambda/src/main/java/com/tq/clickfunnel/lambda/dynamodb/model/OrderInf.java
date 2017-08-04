package com.tq.clickfunnel.lambda.dynamodb.model;

public class OrderInf {

    private Integer contactId;

    private String email;

    private Integer orderId;

    private Integer invoiceId;

    private Integer productId;

    private String createdAt;

    private String updatedAt;

    private Boolean successfull = Boolean.FALSE; // status of credit card charge

    private String message;

    private Integer refNum;

    private String code;

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

    public Boolean getSuccessfull() {
        return successfull;
    }

    public void setSuccessfull(Boolean successfull) {
        this.successfull = successfull;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRefNum() {
        return refNum;
    }

    public void setRefNum(Integer refNum) {
        this.refNum = refNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    //
    public OrderInf withContactId(Integer contactId) {
        this.contactId = contactId;
        return this;
    }

    public OrderInf withEmail(String email) {
        this.email = email;
        return this;
    }

    public OrderInf withOrderId(Integer orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderInf withInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }

    public OrderInf withProductId(Integer productId) {
        this.productId = productId;
        return this;
    }

    public OrderInf withCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public OrderInf withUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public OrderInf withSuccessfull(Boolean successfull) {
        this.successfull = successfull;
        return this;
    }

    public OrderInf withMessage(String message) {
        this.message = message;
        return this;
    }

    public OrderInf withRefNum(Integer refNum) {
        this.refNum = refNum;
        return this;
    }

    public OrderInf withCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String toString() {
        return "OrderInf [contactId=" + contactId + ", email=" + email + ", orderId=" + orderId + ", invoiceId=" + invoiceId
                + ", productId=" + productId + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", successfull=" + successfull
                + ", message=" + message + ", refNum=" + refNum + ", code=" + code + "]";
    }
}
