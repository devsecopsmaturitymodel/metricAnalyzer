package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.Period;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.DateActivity;

public class DatePeriodActivity extends DateActivity {
  @JsonProperty("period")
  protected Period period;

  public Period getPeriod() {
    return period;
  }

  public void setPeriod(Period period) {
    this.period = period;
  }
}
