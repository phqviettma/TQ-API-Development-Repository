package com.tq.inf.query;

import java.util.Map;

public class CountDataQuery {

    private String table;
    private Map<?, ?> filter; // field = condition value

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Map<?, ?> getFilter() {
        return filter;
    }

    public void setFilter(Map<?, ?> filter) {
        this.filter = filter;
    }

    public CountDataQuery withTable(String table) {
        this.table = table;
        return this;
    }

    public CountDataQuery withFilter(Map<?, ?> filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public String toString() {
        return "CountDataQuery [table=" + table + ", filter=" + filter + "]";
    }
}
