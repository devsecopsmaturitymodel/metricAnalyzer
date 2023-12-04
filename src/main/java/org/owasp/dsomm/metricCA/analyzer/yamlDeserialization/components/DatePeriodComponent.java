package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.components;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Component;

import java.util.Date;

public class DatePeriodComponent implements Component<Date> {

    private String name;
    private Date value;

    private String period;

    private boolean isActive = true;

    public DatePeriodComponent() {}

    public DatePeriodComponent(String period) {
        this.setPeriod(period);
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String givenPeriod) {
        this.period = givenPeriod;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
