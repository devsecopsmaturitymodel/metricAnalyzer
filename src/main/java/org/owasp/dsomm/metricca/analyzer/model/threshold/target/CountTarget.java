package org.owasp.dsomm.metricca.analyzer.model.threshold.target;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Target;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Component;

import java.util.ArrayList;

public class CountTarget extends Target {
  @JsonProperty("min value")
  private Integer minValue = Integer.MIN_VALUE;

  @JsonProperty("max value")
  private Integer maxValue = Integer.MAX_VALUE;

  private Integer count;

  public CountTarget() {
  }
  
  public Boolean thresholdReached() {
    if(this.count == null) {
      return null;
    }
    return this.count >= minValue && this.count <= maxValue;
  }


  public void setCount(Integer count) {
    this.count = count;
  }

  public Integer getCount() {
    return count;
  }

  @Override
  public void setThresholdValue(Object count) {
    this.count =(Integer) count;
  }
}
