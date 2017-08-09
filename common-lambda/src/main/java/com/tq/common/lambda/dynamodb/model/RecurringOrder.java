package com.tq.common.lambda.dynamodb.model;

public class RecurringOrder {

    private Integer id;

    /**
     * 
     * At the first time when going to shopping card or API to creating order, the invoice id = originatingOrderId For next month ( auto renew ) on the same
     * subscription/product, we will check the in Job table to find out the Invoice id
     * 
     */
    private Integer originatingOrderId; // as invoice id.

    private Integer productId;

    private String startDate;

    private String endDate;

    private String lastBillDate;

    private String nextBillDate;

    private String status;

    private Integer autoCharge;

    private Integer subscriptionPlanId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOriginatingOrderId() {
        return originatingOrderId;
    }

    public void setOriginatingOrderId(Integer originatingOrderId) {
        this.originatingOrderId = originatingOrderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLastBillDate() {
        return lastBillDate;
    }

    public void setLastBillDate(String lastBillDate) {
        this.lastBillDate = lastBillDate;
    }

    public String getNextBillDate() {
        return nextBillDate;
    }

    public void setNextBillDate(String nextBillDate) {
        this.nextBillDate = nextBillDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAutoCharge() {
        return autoCharge;
    }

    public void setAutoCharge(Integer autoCharge) {
        this.autoCharge = autoCharge;
    }

    public Integer getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlanId(Integer subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
    }

    public RecurringOrder withId(Integer id) {
        this.id = id;
        return this;
    }

    public RecurringOrder withOriginatingOrderId(Integer originatingOrderId) {
        this.originatingOrderId = originatingOrderId;
        return this;
    }

    public RecurringOrder withProductId(Integer productId) {
        this.productId = productId;
        return this;
    }

    public RecurringOrder withStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public RecurringOrder withEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public RecurringOrder withLastBillDate(String lastBillDate) {
        this.lastBillDate = lastBillDate;
        return this;
    }

    public RecurringOrder withNextBillDate(String nextBillDate) {
        this.nextBillDate = nextBillDate;
        return this;
    }

    public RecurringOrder withStatus(String status) {
        this.status = status;
        return this;
    }

    public RecurringOrder withAutoCharge(Integer autoCharge) {
        this.autoCharge = autoCharge;
        return this;
    }

    public RecurringOrder withSubscriptionPlanId(Integer subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
        return this;
    }

    @Override
    public String toString() {
        return "RecurringOrder [id=" + id + ", originatingOrderId=" + originatingOrderId + ", productId=" + productId + ", startDate="
                + startDate + ", endDate=" + endDate + ", lastBillDate=" + lastBillDate + ", nextBillDate=" + nextBillDate + ", status="
                + status + ", autoCharge=" + autoCharge + ", subscriptionPlanId=" + subscriptionPlanId + "]";
    }
}
