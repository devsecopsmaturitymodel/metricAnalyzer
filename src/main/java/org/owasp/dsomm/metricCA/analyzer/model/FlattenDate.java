package org.owasp.dsomm.metricCA.analyzer.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FlattenDate {
    private Date date;
    private final Map<String, Boolean> entries = new HashMap<>();

    public FlattenDate(Date givenDate) {
        this.date = givenDate;
    }

    public Map<String, Boolean> getEntries() {
        return entries;
    }

    public void addDynamicField(String key, Boolean value) {
        entries.put(key, value);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}