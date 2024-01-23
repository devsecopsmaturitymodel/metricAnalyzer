package org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.target;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold.Target;

public class CountHoursAndPeopleTarget extends Target {

  private Integer peopleAndHours;

  @JsonProperty("min value")
  private Integer minValue = Integer.MIN_VALUE;

  @JsonIgnore
  @JsonProperty("implemented")
  public Boolean implemented() {
    if (this.peopleAndHours == null) {
      return null;
    }
    return this.peopleAndHours >= minValue;
  }

  @Override
  public void setThresholdValue(Object peopleAndHours) {
    if (peopleAndHours instanceof Integer)
      this.peopleAndHours = (Integer) peopleAndHours;
  }

  public Integer getMinValue() {
    return minValue;
  }

  public void setMinValue(Integer minValue) {
    this.minValue = minValue;
  }

  public Integer getPeopleAndHours() {
    return peopleAndHours;
  }

  public void setPeopleAndHours(Integer peopleAndHours) {
    this.peopleAndHours = peopleAndHours;
  }

  @Override
  public String getDescription() {
    String description = "Min value: " + getMinValue();
    if (getPeopleAndHours() != null) {
      description += " PeopleAndHours: " + getPeopleAndHours();
    }

    return description;
  }
}
