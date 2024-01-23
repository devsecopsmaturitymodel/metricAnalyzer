package org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.target.CountHoursAndPeopleTarget;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.target.CountTarget;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CountTarget.class, name = "count"),
    @JsonSubTypes.Type(value = CountHoursAndPeopleTarget.class, name = "countHoursAndPeople")
})
public abstract class Target {
  public Target() {
  }

  @JsonProperty("implemented")
  public Boolean implemented() {
    return null;
  }

  public abstract void setThresholdValue(Object count);

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  public abstract String getDescription();
}