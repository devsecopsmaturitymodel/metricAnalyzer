package org.owasp.dsomm.metricca.analyzer.deserialization.activity.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Date {
  private static final Logger logger = LoggerFactory.getLogger(Date.class);
  protected java.util.Date date;

  public java.util.Date getDate() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }
}
