package com.tq.inf.model;

public enum DataTypeInf {
    TEXT("Text"), 
    TEXT_AREA("TextArea"), 
    DATE("Date"),  
    EMAIL("Email"), 
    LIST_BOX("ListBox"),
    RADIO("Radio"),
    DROPDOWN("Dropdown");
    
    private String type;

    DataTypeInf(String type) {
        this.setType(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
