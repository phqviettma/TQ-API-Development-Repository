package com.tq.inf.query;

import java.util.List;

public class LoadDataQuery {

    private String table;
    private Integer recordID;
    private List<?> selectedFields;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Integer getRecordID() {
        return recordID;
    }

    public void setRecordID(Integer recordID) {
        this.recordID = recordID;
    }

    public List<?> getSelectedFields() {
        return selectedFields;
    }

    public void setSelectedFields(List<?> selectedFields) {
        this.selectedFields = selectedFields;
    }

    public LoadDataQuery withTable(String table) {
        this.table = table;
        return this;
    }

    public LoadDataQuery withRecordID(Integer recordID) {
        this.recordID = recordID;
        return this;
    }

    public LoadDataQuery withSelectedFields(List<?> selectedFields) {
        this.selectedFields = selectedFields;
        return this;
    }

    @Override
    public String toString() {
        return "LoadDataQuery [table=" + table + ", recordID=" + recordID + ", selectedFields=" + selectedFields + "]";
    }
    
}
