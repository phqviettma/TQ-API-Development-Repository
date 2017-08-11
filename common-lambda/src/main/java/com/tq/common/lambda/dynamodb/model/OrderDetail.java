package com.tq.common.lambda.dynamodb.model;

import java.util.List;

public class OrderDetail{
    
    private Integer contactId;

    private Integer orderIdInf;

    private Integer invoiceInf;

    private List<Integer> productIdInfs;

    private Integer productCf;

    private String createdAt;

    private String updatedAt;

    private String successful;

    private String message;

    private String refNum;

    private String code;
    

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }
    
    public OrderDetail withContactId(Integer contactId) {
        this.contactId = contactId;
        return this;
    }

    public Integer getOrderIdInf() {
        return orderIdInf;
    }

    public void setOrderIdInf(Integer orderIdInf) {
        this.orderIdInf = orderIdInf;
    }

    public OrderDetail withOrderIdInf(Integer orderIdInf) {
        this.orderIdInf = orderIdInf;
        return this;
    }

    public Integer getInvoiceInf() {
        return invoiceInf;
    }

    public void setInvoiceInf(Integer invoiceInf) {
        this.invoiceInf = invoiceInf;
    }

    public OrderDetail withInvoiceInf(Integer invoiceInf) {
        this.invoiceInf = invoiceInf;
        return this;
    }

    public List<Integer> getProductIdInfs() {
        return productIdInfs;
    }

    public void setProductIdInfs(List<Integer> productIdInfs) {
        this.productIdInfs = productIdInfs;
    }

    public OrderDetail withProductIdInf(List<Integer> productIdInfs) {
        this.productIdInfs = productIdInfs;
        return this;
    }

    public Integer getProductCf() {
        return productCf;
    }

    public void setProductCf(Integer productCf) {
        this.productCf = productCf;
    }

    public OrderDetail withProductCf(Integer productCf) {
        this.productCf = productCf;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public OrderDetail withCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OrderDetail withUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getSuccessful() {
        return successful;
    }

    public void setSuccessful(String successful) {
        this.successful = successful;
    }

    public OrderDetail withSuccessful(String successful) {
        this.successful = successful;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderDetail withMessage(String message) {
        this.message = message;
        return this;
    }

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    public OrderDetail withRefNum(String refNum) {
        this.refNum = refNum;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OrderDetail withCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String toString() {
        return "OrderDetail [contactId=" + contactId + ", orderIdInf=" + orderIdInf + ", invoiceInf=" + invoiceInf + ", productIdInfs="
                + productIdInfs + ", productCf=" + productCf + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", successful="
                + successful + ", message=" + message + ", refNum=" + refNum + ", code=" + code + "]";
    }
}
