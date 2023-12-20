package org.owasp.dsomm.metricca.analyzer.model.threshold;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

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

  @JsonIgnore // Java has problems with jodatime
  public Period getPeriodAsPeriod() {
    PeriodFormatter formatter = new PeriodFormatterBuilder()
        .appendYears().appendSuffix("y")
        .appendMonths().appendSuffix("m")
        .appendWeeks().appendSuffix("w")
        .appendDays().appendSuffix("d")
        .appendHours().appendSuffix("h")
        .toFormatter();

    return formatter.parsePeriod(this.period);
  }

  public void setPeriod(String period) {
    this.period = period;
  }
}
