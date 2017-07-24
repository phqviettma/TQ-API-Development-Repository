package com.tq.inf.model;

/**
 * 
 * Do you want to add customer field which table.
 * Inf just support some tables as below
 */
public enum CustomFieldType {
    CONTACT("Contact"), 
    COMPANY("Company"), 
    ORDER("Order"), 
    TASK_APP_NOTE("Task/App/Note"), 
    SUBSCRIPTION("Subscription"), 
    OPPORTUNITY("Opportunity"), 
    REFERRAL_PARNER("Referral Parner");
    
    private String table;

    CustomFieldType(String table) {
        this.setTable(table);
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

}
