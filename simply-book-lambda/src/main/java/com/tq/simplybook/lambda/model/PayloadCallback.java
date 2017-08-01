package com.tq.simplybook.lambda.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PayloadCallback implements Serializable {
    
    private static final long serialVersionUID = 3285019718457403417L;

    private Long booking_id;

    private String booking_hash;

    private String company;

    /**
     * create, cancel, notify, change
     */
    private String notification_type;

    public Long getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(Long booking_id) {
        this.booking_id = booking_id;
    }

    public String getBooking_hash() {
        return booking_hash;
    }

    public void setBooking_hash(String booking_hash) {
        this.booking_hash = booking_hash;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    @Override
    public String toString() {
        return "PayloadCallback [booking_id=" + booking_id + ", booking_hash=" + booking_hash + ", company=" + company
                + ", notification_type=" + notification_type + "]";
    }
}
