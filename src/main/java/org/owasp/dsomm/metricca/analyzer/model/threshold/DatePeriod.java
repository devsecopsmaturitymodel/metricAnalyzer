package org.owasp.dsomm.metricca.analyzer.model.threshold;

import com.fasterxml.jackson.annotation.JsonProperty;

//TODO Mapping für calendar / sliding
public class DatePeriod {
  @JsonProperty("period type")
  private String periodType; // calendar / sliding
  @JsonProperty("period")
  private String period;

  public DatePeriod() {
  }

  public String getPeriodType() {
    return periodType;
  }

  public void setPeriodType(String periodType) {
    this.periodType = periodType;
  }

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }
}
