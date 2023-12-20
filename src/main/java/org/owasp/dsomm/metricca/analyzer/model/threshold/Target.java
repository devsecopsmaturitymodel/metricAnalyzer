package org.owasp.dsomm.metricca.analyzer.model.threshold;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.owasp.dsomm.metricca.analyzer.model.threshold.target.CountTarget;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Component;

import java.util.ArrayList;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CountTarget.class, name = "count"),
})
public abstract class Target {
  public Target() {
  }

  public Boolean thresholdReached() {
    return null;
  }

  public abstract void setThresholdValue(Object count);
}

