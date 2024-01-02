package org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

//TODO Mapping für calendar / sliding
public class Period {
  @JsonProperty("period type")
  private String periodType; // calendar / sliding
  @JsonProperty("timeframe")
  private String timeframe;

  public Period() {
  }

  public String getPeriodType() {
    return periodType;
  }

  public void setPeriodType(String periodType) {
    this.periodType = periodType;
  }

  public String getTimeframe() {
    return timeframe;
  }

  public void setTimeframe(String timeframe) {
    this.timeframe = timeframe;
  }

  @JsonIgnore // Java has problems with jodatime
  public org.joda.time.Period getPeriodAsPeriod() {
    PeriodFormatter formatter = new PeriodFormatterBuilder()
        .appendYears().appendSuffix("y")
        .appendMonths().appendSuffix("m")
        .appendWeeks().appendSuffix("w")
        .appendDays().appendSuffix("d")
        .appendHours().appendSuffix("h")
        .toFormatter();

    return formatter.parsePeriod(this.timeframe);
  }
}
