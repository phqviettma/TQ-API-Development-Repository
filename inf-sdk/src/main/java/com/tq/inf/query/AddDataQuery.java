package com.tq.inf.query;

import java.util.Map;

public class AddDataQuery {

    private String table;
    private Integer recordID = null; // used for update method on the given record
    private Map<?, ?> dataRecord;

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

    public Map<?, ?> getDataRecord() {
        return dataRecord;
    }

    public void setDataRecord(Map<?, ?> dataRecord) {
        this.dataRecord = dataRecord;
    }

    public AddDataQuery withTable(String table) {
        this.table = table;
        return this;
    }

    public AddDataQuery withRecordID(Integer recordID) {
        this.recordID = recordID;
        return this;
    }

    public AddDataQuery withDataRecord(Map<?, ?> dataRecord) {
        this.dataRecord = dataRecord;
        return this;
    }

    @Override
    public String toString() {
        return "AddDataQuery [table=" + table + ", recordID=" + recordID + ", dataRecord=" + dataRecord + "]";
    }
}
