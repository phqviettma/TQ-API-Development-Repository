package com.tq.inf.query;

import java.util.List;

public class SearchEmailQuery {

    private String email;
    private List<String> selectedFields;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<?> getSelectedFields() {
        return selectedFields;
    }

    public void setSelectedFields(List<String> selectedFields) {
        this.selectedFields = selectedFields;
    }

    public SearchEmailQuery withEmail(String email) {
        this.email = email;
        return this;
    }

    public SearchEmailQuery withSelectedFields(List<String> selectedFields) {
        this.selectedFields = selectedFields;
        return this;
    }

    @Override
    public String toString() {
        return "SearchEmailQuery [email=" + email + ", selectedFields=" + selectedFields + "]";
    }
}
