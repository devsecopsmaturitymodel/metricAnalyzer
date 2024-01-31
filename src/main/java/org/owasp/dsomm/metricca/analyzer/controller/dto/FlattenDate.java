package org.owasp.dsomm.metricca.analyzer.controller.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FlattenDate {
  private final Map<String, Object> entries = new HashMap<>();
  private Date date;

  public FlattenDate(Date date) {
    this.date = date;
  }

  public Map<String, Object> getEntries() {
    return entries;
  }


  public void addDynamicField(String key, Object value) {
    entries.put(key, value);
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}