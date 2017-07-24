package com.tq.inf.query;

import java.util.Map;

import com.tq.inf.model.DetermineOption;

public class AddNewContactQuery {

    private Map<?, ?> dataRecord; // [{ Field = value }]
    private DetermineOption dupCheckType = DetermineOption.Email; // default based  on email

    public Map<?, ?> getDataRecord() {
        return dataRecord;
    }

    public void setDataRecord(Map<?, ?> dataRecord) {
        this.dataRecord = dataRecord;
    }
    
    public DetermineOption getDupCheckType() {
        return dupCheckType;
    }

    public void setDupCheckType(DetermineOption dupCheckType) {
        this.dupCheckType = dupCheckType;
    }

    public AddNewContactQuery withDataRecord(Map<?, ?> dataRecord) {
        this.dataRecord = dataRecord;
        return this;
    }
    
    public AddNewContactQuery withDuplicateCheck(DetermineOption dupCheckType) {
        this.dupCheckType = dupCheckType;
        return this;
    }

    @Override
    public String toString() {
        return "AddNewContactQuery [dataRecord=" + dataRecord + "]";
    }

}
