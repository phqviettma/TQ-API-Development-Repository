package com.tq.clickfunnel.lambda.modle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CFCurrency {

    @JsonProperty("id")
    private String id;

    @JsonProperty("decimal_mark")
    private String decimalMark;

    @JsonProperty("iso_code")
    private String isoCode;

    @JsonProperty("iso_numeric")
    private Integer isoNumeric;

    @JsonProperty("name")
    private String name;

    @JsonProperty("priority")
    private Integer priority;

    @JsonProperty("smallest_denomination")
    private Integer smallestDenomination;

    @JsonProperty("subunit")
    private String subunit;

    @JsonProperty("subunit_to_unit")
    private Integer subunitToUnit;

    @JsonProperty("symbol")
    private String symbol;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDecimalMark() {
        return decimalMark;
    }

    public void setDecimalMark(String decimalMark) {
        this.decimalMark = decimalMark;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public Integer getIsoNumeric() {
        return isoNumeric;
    }

    public void setIsoNumeric(Integer isoNumeric) {
        this.isoNumeric = isoNumeric;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getSmallestDenomination() {
        return smallestDenomination;
    }

    public void setSmallestDenomination(Integer smallestDenomination) {
        this.smallestDenomination = smallestDenomination;
    }

    public String getSubunit() {
        return subunit;
    }

    public void setSubunit(String subunit) {
        this.subunit = subunit;
    }

    public Integer getSubunitToUnit() {
        return subunitToUnit;
    }

    public void setSubunitToUnit(Integer subunitToUnit) {
        this.subunitToUnit = subunitToUnit;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "CFCurrency [id=" + id + ", decimalMark=" + decimalMark + ", isoCode=" + isoCode + ", isoNumeric=" + isoNumeric + ", name="
                + name + ", priority=" + priority + ", smallestDenomination=" + smallestDenomination + ", subunit=" + subunit
                + ", subunitToUnit=" + subunitToUnit + ", symbol=" + symbol + "]";
    }
}
