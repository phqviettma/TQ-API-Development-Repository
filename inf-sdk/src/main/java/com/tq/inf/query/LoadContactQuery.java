package com.tq.inf.query;

import java.util.List;

public class LoadContactQuery {
    private Integer contactID;
    private List<?> selectedFields;

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    public List<?> getSelectedFields() {
        return selectedFields;
    }

    public void setSelectedFields(List<?> selectedFields) {
        this.selectedFields = selectedFields;
    }

    public LoadContactQuery withContactId(Integer contactId) {
        this.contactID = contactId;
        return this;
    }

    public LoadContactQuery withSelectedFields(List<?> selectedFields) {
        this.selectedFields = selectedFields;
        return this;
    }

    @Override
    public String toString() {
        return "LoadContactQuery [contactID=" + contactID + ", selectedFields=" + selectedFields + "]";
    }
}
