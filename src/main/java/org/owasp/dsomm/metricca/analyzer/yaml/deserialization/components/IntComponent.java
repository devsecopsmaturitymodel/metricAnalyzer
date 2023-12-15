package org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Component;

public class IntComponent implements Component<Integer> {
  private String name;
  private int value;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(String newName) {
    this.name = newName;
  }

  @Override
  public Integer getValue() {
    return this.value;
  }

  @Override
  public void setValue(Object value) {
    int s = (Integer) value;
    this.value = s;
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
