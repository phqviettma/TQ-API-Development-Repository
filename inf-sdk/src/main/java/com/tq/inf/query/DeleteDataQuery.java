package com.tq.inf.query;

public class DeleteDataQuery {

    private String table;
    private Integer recordID;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Integer getRecordID() {
        return recordID;
    }

    public void RecordID(Integer recordID) {
        this.recordID = recordID;
    }

    public DeleteDataQuery withTable(String table) {
        this.table = table;
        return this;
    }

    public DeleteDataQuery withRecordID(Integer recordID) {
        this.recordID = recordID;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteDataQuery [table=" + table + ", recordID=" + recordID + "]";
    }

}
