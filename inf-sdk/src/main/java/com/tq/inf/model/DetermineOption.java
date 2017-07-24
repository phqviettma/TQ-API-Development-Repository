package com.tq.inf.model;

public enum DetermineOption {
    Email("Email"), EmailAndName("EmailAndName"), EmailAndNameAndCompany("EmailAndNameAndCompany");

    private String option;

    DetermineOption(String option) {
        this.setOption(option);
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
