package org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

public class DatePeriodEndComponent extends DatePeriodComponent implements Component<Date> {
  private static final Logger logger = LoggerFactory.getLogger(DatePeriodEndComponent.class);

  @JsonIgnore
  private Period period;

  private boolean isActive = false;

  public DatePeriodEndComponent(String period) {
    super(period);
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

  @JsonIgnore
  public Period getPeriodAsPeriod() {
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
