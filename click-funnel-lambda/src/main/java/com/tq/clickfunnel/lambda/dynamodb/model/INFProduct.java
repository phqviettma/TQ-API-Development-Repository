package com.tq.clickfunnel.lambda.dynamodb.model;

import java.util.List;

/**
 * 
 * @author phqviet
 * Model Product in infusion soft
 */
public class INFProduct {

    /**
     * Production ID in Infusion soft
     */
    private List<Integer> productIds;

    private String productName;

    private Double productPrice;

    private String sku;

    private String description;

    private  List<Integer> subscriptionPlanIds;

    private Integer cartId;

    private Integer planId;

    private String productType;
    
    public  List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds( List<Integer> productIds) {
        this.productIds = productIds;
    }
    
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public  List<Integer> getSubscriptionPlanIds() {
        return subscriptionPlanIds;
    }

    public void setSubscriptionPlanIds( List<Integer> subscriptionPlanIds) {
        this.subscriptionPlanIds = subscriptionPlanIds;
    }

    @Override
    public String toString() {
        return "INFProduct [productIds=" + productIds + ", productName=" + productName + ", productPrice=" + productPrice + ", sku=" + sku
                + ", description=" + description + ", subscriptionPlanIds=" + subscriptionPlanIds + ", cartId=" + cartId + ", planId="
                + planId + ", productType=" + productType + "]";
    }
}
