package com.tq.inf.query;

public class ApplyTagQuery {

    private Integer contactID;
    private Integer tagID;

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    public Integer getTagID() {
        return tagID;
    }

    public void setTagID(Integer tagID) {
        this.tagID = tagID;
    }

    public ApplyTagQuery withContactID(Integer contactID) {
        this.contactID = contactID;
        return this;
    }

    public ApplyTagQuery withTagID(Integer tagID) {
        this.tagID = tagID;
        return this;
    }

    @Override
    public String toString() {
        return "ApplyTagQuery [contactID=" + contactID + ", tagID=" + tagID + "]";
    }
}
