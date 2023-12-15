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

  public CountTarget() {
  }

  public Boolean thresholdReached(ArrayList<Component> componentArrayList) {
    ArrayList<Component> components = getComponentsForTarget(componentArrayList);
    return components.size() >= minValue && components.size() <= maxValue;
  }
}
