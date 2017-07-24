package com.tq.inf.query;

public class OptQuery {
    private String email;
    // what is the reason for OptIn/Optout under the apiName/apiKey
    private String reasion;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReasion() {
        return reasion;
    }

    public void setReasion(String reasion) {
        this.reasion = reasion;
    }

    public OptQuery withEmail(String email) {
        this.email = email;
        return this;
    }

    public OptQuery withReasion(String reasion) {
        this.reasion = reasion;
        return this;
    }

    @Override
    public String toString() {
        return "OptQuery [email=" + email + ", reasion=" + reasion + "]";
    }
}
