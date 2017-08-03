package com.tq.clickfunnel.lambda.modle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CFAmount {
    
    private Double fractional;
    
    private CFCurrency currency;

    public Double getFractional() {
        return fractional;
    }

    public void setFractional(Double fractional) {
        this.fractional = fractional;
    }

    public CFCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(CFCurrency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "CFAmount [fractional=" + fractional + ", currency=" + currency + "]";
    }
}
