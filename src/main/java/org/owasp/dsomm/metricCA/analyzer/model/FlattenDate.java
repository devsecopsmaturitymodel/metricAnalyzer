package org.owasp.dsomm.metricCA.analyzer.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FlattenDate {
  private final Map<String, Object> dynamicFields = new HashMap<>();
  private Date givenDate;

  public FlattenDate(Date givenDate) {
    this.givenDate = givenDate;
  }

  @JsonAnyGetter
  public Map<String, Object> getDynamicFields() {
    return dynamicFields;
  }

  public void addDynamicField(String key, Object value) {
    dynamicFields.put(key, value);
  }

  public Date getGivenDate() {
    return givenDate;
  }

  public void setGivenDate(Date givenDate) {
    this.givenDate = givenDate;
  }

}