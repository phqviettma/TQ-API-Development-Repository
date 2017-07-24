package com.tq.inf.query;

import com.tq.inf.model.CustomFieldType;
import com.tq.inf.model.DataTypeInf;

public class CustomFieldDataQuery {

    private CustomFieldType customFieldType;
    private String displayName;
    private DataTypeInf dataType;
    private Integer headerID;

    public CustomFieldType getCustomFieldType() {
        return customFieldType;
    }

    public void setCustomFieldType(CustomFieldType customFieldType) {
        this.customFieldType = customFieldType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public DataTypeInf getDataType() {
        return dataType;
    }

    public void setDataType(DataTypeInf dataType) {
        this.dataType = dataType;
    }

    public Integer getHeaderID() {
        return headerID;
    }

    public void setHeaderID(Integer headerID) {
        this.headerID = headerID;
    }

    public CustomFieldDataQuery withCustomFieldType(CustomFieldType customFieldType) {
        this.customFieldType = customFieldType;
        return this;
    }

    public CustomFieldDataQuery withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public CustomFieldDataQuery withDataType(DataTypeInf dataType) {
        this.dataType = dataType;
        return this;
    }

    public CustomFieldDataQuery withHeaderID(Integer headerID) {
        this.headerID = headerID;
        return this;
    }

    @Override
    public String toString() {
        return "CustomFieldDataQuery [customFieldType=" + customFieldType + ", displayName=" + displayName + ", dataType=" + dataType
                + ", headerID=" + headerID + "]";
    }
}
