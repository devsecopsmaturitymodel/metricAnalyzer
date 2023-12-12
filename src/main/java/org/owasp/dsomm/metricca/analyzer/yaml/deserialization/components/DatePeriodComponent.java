package org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components;

import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Component;

import java.util.Date;

public class DatePeriodComponent extends DateComponent implements Component<Date> {

  private String period;

  private boolean isActive = true;

  public DatePeriodComponent(String period) {
    this.setPeriod(period);
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
