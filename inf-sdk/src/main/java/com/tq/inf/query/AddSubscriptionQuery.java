package com.tq.inf.query;

public class AddSubscriptionQuery {

    private Integer contactID;
    private Boolean allowDup;
    // Allows a duplicate subscription or not
    private Integer subscriptionPlanID;
    private Integer qty;
    private Double price;
    private Boolean allowTax;
    private Integer merchantID;
    private Integer creditCardID;
    private Integer affiliateID = 0;
    private Integer dayCharge; // Number of days the subscription will trial 

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    public Boolean getAllowDup() {
        return allowDup;
    }

    public void setAllowDup(Boolean allowDup) {
        this.allowDup = allowDup;
    }

    public Integer getSubscriptionPlanID() {
        return subscriptionPlanID;
    }

    public void setSubscriptionPlanID(Integer subscriptionPlanID) {
        this.subscriptionPlanID = subscriptionPlanID;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getAllowTax() {
        return allowTax;
    }

    public void setAllowTax(Boolean allowTax) {
        this.allowTax = allowTax;
    }

    public Integer getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(Integer merchantID) {
        this.merchantID = merchantID;
    }

    public Integer getCreditCardID() {
        return creditCardID;
    }

    public void setCreditCardID(Integer creditCardID) {
        this.creditCardID = creditCardID;
    }

    public Integer getAffiliateID() {
        return affiliateID;
    }

    public void setAffiliateID(Integer affiliateID) {
        this.affiliateID = affiliateID;
    }

    public Integer getDayCharge() {
        return dayCharge;
    }

    public void setDayCharge(Integer dayCharge) {
        this.dayCharge = dayCharge;
    }

    public AddSubscriptionQuery withContactID(Integer contactID) {
        this.contactID = contactID;
        return this;
    }

    public AddSubscriptionQuery withAllowDup(Boolean allowDup) {
        this.allowDup = allowDup;
        return this;
    }

    public AddSubscriptionQuery withSubscriptionPlanID(Integer subscriptionPlanID) {
        this.subscriptionPlanID = subscriptionPlanID;
        return this;
    }

    public AddSubscriptionQuery withQty(Integer qty) {
        this.qty = qty;
        return this;
    }

    public AddSubscriptionQuery withPrice(Double price) {
        this.price = price;
        return this;
    }

    public AddSubscriptionQuery withAllowTax(Boolean allowTax) {
        this.allowTax = allowTax;
        return this;
    }

    public AddSubscriptionQuery withMerchantID(Integer merchantID) {
        this.merchantID = merchantID;
        return this;
    }

    public AddSubscriptionQuery withCreditCardID(Integer creditCardID) {
        this.creditCardID = creditCardID;
        return this;
    }

    public AddSubscriptionQuery withAffiliateID(Integer affiliateID) {
        this.affiliateID = affiliateID;
        return this;
    }

    public AddSubscriptionQuery withDayCharge(Integer dayCharge) {
        this.dayCharge = dayCharge;
        return this;
    }

    @Override
    public String toString() {
        return "AddSubscriptionQuery [contactID=" + contactID + ", allowDup=" + allowDup + ", subscriptionPlanID=" + subscriptionPlanID
                + ", qty=" + qty + ", price=" + price + ", allowTax=" + allowTax + ", merchantID=" + merchantID + ", creditCardID="
                + creditCardID + ", affiliateID=" + affiliateID + ", dayCharge=" + dayCharge + "]";
    }

}
