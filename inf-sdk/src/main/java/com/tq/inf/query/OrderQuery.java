package com.tq.inf.query;

import java.util.List;

public class OrderQuery {

    private Integer contactID;
    private Integer cardID;
    private Integer planID;
    private List<Integer> productionIDs;
    private List<Integer> subscriptionIDs;
    private Boolean processSpecials;
    private List<String> promoCodes;
    private Integer leadAffiliateID;
    private Integer saleAffiliateID;

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    public Integer getCardID() {
        return cardID;
    }

    public void setCardID(Integer cardID) {
        this.cardID = cardID;
    }

    public Integer getPlanID() {
        return planID;
    }

    public void setPlanID(Integer planID) {
        this.planID = planID;
    }

    public List<Integer> getProductionIDs() {
        return productionIDs;
    }

    public void setProductionIDs(List<Integer> productionIDs) {
        this.productionIDs = productionIDs;
    }

    public List<Integer> getSubscriptionIDs() {
        return subscriptionIDs;
    }

    public void setSubscriptionIDs(List<Integer> subscriptionIDs) {
        this.subscriptionIDs = subscriptionIDs;
    }

    public Boolean getProcessSpecials() {
        return processSpecials;
    }

    public void setProcessSpecials(Boolean processSpecials) {
        this.processSpecials = processSpecials;
    }

    public List<String> getPromoCodes() {
        return promoCodes;
    }

    public void setPromoCodes(List<String> promoCodes) {
        this.promoCodes = promoCodes;
    }

    public Integer getLeadAffiliateID() {
        return leadAffiliateID;
    }

    public void setLeadAffiliateID(Integer leadAffiliateID) {
        this.leadAffiliateID = leadAffiliateID;
    }

    public Integer getSaleAffiliateID() {
        return saleAffiliateID;
    }

    public void setSaleAffiliateID(Integer saleAffiliateID) {
        this.saleAffiliateID = saleAffiliateID;
    }

    public OrderQuery withContactID(Integer contactId) {
        this.contactID = contactId;
        return this;
    }

    public OrderQuery withCardID(Integer cardID) {
        this.cardID = cardID;
        return this;
    }

    public OrderQuery withPlanID(Integer planID) {
        this.planID = planID;
        return this;
    }

    public OrderQuery withProductionIDs(List<Integer> productionIDs) {
        this.productionIDs = productionIDs;
        return this;
    }

    public OrderQuery withSubscriptionIDs(List<Integer> subscriptionIDs) {
        this.subscriptionIDs = subscriptionIDs;
        return this;
    }

    public OrderQuery withProcessSpecials(Boolean processSpecials) {
        this.processSpecials = processSpecials;
        return this;
    }

    public OrderQuery withPromoCodes(List<String> promoCodes) {
        this.promoCodes = promoCodes;
        return this;
    }

    public OrderQuery withLeadAffiliateID(Integer leadAffiliateID) {
        this.leadAffiliateID = leadAffiliateID;
        return this;
    }

    public OrderQuery withSaleAffiliateID(Integer saleAffiliateID) {
        this.saleAffiliateID = saleAffiliateID;
        return this;
    }

    @Override
    public String toString() {
        return "OrderQuery [contactID=" + contactID + ", cardID=" + cardID + ", planID=" + planID + ", productionIDs=" + productionIDs
                + ", subscriptionIDs=" + subscriptionIDs + ", processSpecials=" + processSpecials + ", promoCodes=" + promoCodes
                + ", leadAffiliateID=" + leadAffiliateID + ", saleAffiliateID=" + saleAffiliateID + "]";
    }
}
