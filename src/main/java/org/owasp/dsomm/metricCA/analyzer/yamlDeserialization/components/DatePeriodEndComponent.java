package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Component;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.YamlToObjectManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

public class DatePeriodEndComponent implements Component<Date> {
    private static final Logger logger = LoggerFactory.getLogger(DatePeriodEndComponent.class);

    private String name;
    private Date value;

    @JsonIgnore
    private Period period;

    private boolean isActive = false;

    public DatePeriodEndComponent() {
    }
    public DatePeriodEndComponent(String period) {
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
        Date givenDate = (Date) value;
        Calendar c = Calendar.getInstance();
        c.setTime(givenDate);
        c.add(Calendar.HOUR, this.period.getHours());
        c.add(Calendar.DATE, this.period.getDays()); // TODO Check
        c.add(Calendar.MONTH, this.period.getMonths());
        c.add(Calendar.YEAR, this.period.getYears());
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

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public void setPeriod(String givenPeriod) {
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendYears().appendSuffix("y")
                .appendMonths().appendSuffix("m")
                .appendWeeks().appendSuffix("w")
                .appendDays().appendSuffix("d")
                .appendHours().appendSuffix("h")
                .toFormatter();

        this.period = formatter.parsePeriod(givenPeriod);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
