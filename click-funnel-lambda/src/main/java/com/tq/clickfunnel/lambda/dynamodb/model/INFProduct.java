package com.tq.clickfunnel.lambda.dynamodb.model;
/**
 * 
 * @author phqviet
 * Model Product in infusion soft
 */
public class INFProduct {

    /**
     * Production ID in Infusion soft
     */
    private Integer id;

    private String productName;

    private Double productPrice;

    private String sku;

    private String description;

    private Integer subscriptionPlanId;

    private Integer cartId;

    private Integer planId;

    private String productType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlanId(Integer subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
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

    @Override
    public String toString() {
        return "INFProduct [id=" + id + ", productName=" + productName + ", productPrice=" + productPrice + ", sku=" + sku
                + ", description=" + description + ", subscriptionPlanId=" + subscriptionPlanId + ", cartId=" + cartId + ", planId="
                + planId + ", productType=" + productType + "]";
    }
}
