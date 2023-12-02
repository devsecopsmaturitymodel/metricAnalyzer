package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.components;

import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class DatePeriodEndComponent extends DatePeriodComponent implements Component<Date> {

    private String name;
    private Date value;

    private int periodInDays;

    private boolean isActive = false;

    public DatePeriodEndComponent() {
    }
    public DatePeriodEndComponent(int periodInDays, boolean isActive) {
        this.periodInDays = periodInDays;
        this.isActive = isActive;
    }

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
        Date givenDate = (Date) value;
        Calendar c = Calendar.getInstance();
        c.setTime(givenDate);
        c.add(Calendar.DATE, this.periodInDays);
        this.value = c.getTime();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();  // Performs a shallow copy
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[name=" + name + ", value=" + value + "]";
    }

    public int getPeriodInDays() {
        return periodInDays;
    }

    public void setPeriodInDays(int periodInDays) {
        this.periodInDays = periodInDays;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
