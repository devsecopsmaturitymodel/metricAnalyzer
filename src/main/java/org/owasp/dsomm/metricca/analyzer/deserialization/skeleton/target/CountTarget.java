package org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.target;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold.Target;

public class CountTarget extends Target {
  @JsonProperty("min value")
  private Integer minValue = Integer.MIN_VALUE;

  @JsonProperty("max value")
  private Integer maxValue = Integer.MAX_VALUE;

  private Integer count;

  public CountTarget() {
  }

  @JsonIgnore
  @JsonProperty("implemented")
  public Boolean implemented() {
    if (this.count == null) {
      return null;
    }
    return this.count >= minValue && this.count <= maxValue;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  @Override
  public void setThresholdValue(Object count) {
    if (count instanceof Integer)
      this.count = (Integer) count;
  }

  @Override
  public String getDescription() {
    String description = "";
    if (getMinValue() != Integer.MIN_VALUE) {
      description += "Min value: " + getMinValue() + " ";
    }
    if (getMaxValue() != Integer.MAX_VALUE) {
      description += "Max value: " + getMaxValue() + " ";
    }
    if (count != null) {
      description += " count: " + getCount() + "\n";
    }

    return description;
  }

  public Integer getMaxValue() {
    return maxValue;
  }

  public Integer getMinValue() {
    return minValue;
  }
}
