package org.owasp.dsomm.metricca.analyzer.model.threshold.target;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Target;

public class IntTarget extends Target {
  @JsonProperty("min value")
  private Integer minValue;

  @JsonProperty("max value")
  private Integer maxValue;

  public IntTarget() {
  }

  public Boolean thresholdReached() {
    return true;
  }
}
