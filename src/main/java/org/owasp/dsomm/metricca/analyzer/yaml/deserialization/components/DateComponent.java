package org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components;

import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Component;

import java.util.Date;

public class DateComponent implements Component<Date> {

  protected String name;
  protected Date value;

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
