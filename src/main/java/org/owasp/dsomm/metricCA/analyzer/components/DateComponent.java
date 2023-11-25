package org.owasp.dsomm.metricCA.analyzer.components;

import org.owasp.dsomm.metricCA.analyzer.Component;

import java.util.Date;

public class DateComponent implements Component<Date> {
    
    private String name;
    private Date value;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String newName) {
        this.name = newName;
    }

    @Override
    public Date getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        Date l = (Date) value;
        this.value = l;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();  // Performs a shallow copy
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[name=" + name + ", value=" + value + "]";
    }
}
