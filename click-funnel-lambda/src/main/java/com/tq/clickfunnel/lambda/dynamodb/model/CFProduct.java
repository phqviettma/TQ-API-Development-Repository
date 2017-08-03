package com.tq.clickfunnel.lambda.dynamodb.model;

public class CFProduct {

    private Integer id;

    private String name;

    private String currency;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "CFProduct [id=" + id + ", name=" + name + ", currency=" + currency + "]";
    }
}
