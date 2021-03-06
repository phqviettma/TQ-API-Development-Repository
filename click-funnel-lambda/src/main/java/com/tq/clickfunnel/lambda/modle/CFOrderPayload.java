package com.tq.clickfunnel.lambda.modle;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CFOrderPayload {
    
    @JsonProperty("id")
    private Integer id; // as purchase id
    
    @JsonProperty("event")
    private String event;
    
    @JsonProperty("products")
    private List<CFProducts> products;
    
    @JsonProperty("purchase")
    private CFPurchase purchase;
    
    @JsonProperty("contact")
    private CFContact contact;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public CFPurchase getPurchase() {
        return purchase;
    }

    public void setPurchase(CFPurchase purchase) {
        this.purchase = purchase;
    }

    public List<CFProducts> getProducts() {
        return products;
    }

    public void setProducts(List<CFProducts> products) {
        this.products = products;
    }

    public CFContact getContact() {
        return contact;
    }

    public void setContact(CFContact contact) {
        this.contact = contact;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CFOrderPayload [id=" + id + ", event=" + event + ", products=" + products + ", purchase=" + purchase + ", contact="
                + contact + "]";
    }
}
