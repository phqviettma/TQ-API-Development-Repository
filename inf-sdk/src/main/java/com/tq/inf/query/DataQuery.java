package com.tq.inf.query;

import java.util.List;
import java.util.Map;

public class DataQuery {

    private Integer limit = 1000;
    private Integer page = 0;
    private String table;
    private List<String> selectedFields;
    private Map<?, ?> filter;
    private String orderBy; // sorted based on which column
    private Boolean ascending;//Boolean.FALSE or Boolean.TRUE // defaults descending

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<String> getSelectedFields() {
        return selectedFields;
    }

    public void setSelectedFields(List<String> selectedFields) {
        this.selectedFields = selectedFields;
    }

    public Map<?, ?> getFilter() {
        return filter;
    }

    public void setFilter(Map<?, ?> filter) {
        this.filter = filter;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Boolean getAscending() {
        return ascending;
    }

    public void setAscending(Boolean ascending) {
        this.ascending = ascending;
    }

    public DataQuery withLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public DataQuery withPage(Integer page) {
        this.page = page;
        return this;
    }

    public DataQuery withTable(String table) {
        this.table = table;
        return this;
    }

    public DataQuery withSelectedFields(List<String> selectedFields) {
        this.selectedFields = selectedFields;
        return this;
    }

    public DataQuery withFilter(Map<?, ?> filter) {
        this.filter = filter;
        return this;
    }

    public DataQuery withOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public DataQuery withAscending(Boolean ascending) {
        this.ascending = ascending;
        return this;
    }

    @Override
    public String toString() {
        return "DataQuery [limit=" + limit + ", page=" + page + ", table=" + table + ", selectedFields=" + selectedFields + ", filter="
                + filter + ", orderBy=" + orderBy + ", ascending=" + ascending + "]";
    }
}
