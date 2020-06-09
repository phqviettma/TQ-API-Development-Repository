package com.tq.inf.query;

public class OptQuery {
    private String email;
    // what is the reason for OptIn/Optout under the apiName/apiKey
    private String reason;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReasion() {
        return reason;
    }

    public void setReasion(String reasion) {
        this.reason = reasion;
    }

    public OptQuery withEmail(String email) {
        this.email = email;
        return this;
    }

    public OptQuery withReasion(String reasion) {
        this.reason = reasion;
        return this;
    }

    @Override
    public String toString() {
        return "OptQuery [email=" + email + ", reason=" + reason + "]";
    }
}
