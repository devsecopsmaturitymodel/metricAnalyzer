package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.DatePeriodActivity;

public class SecurityTrainingActivity extends DatePeriodActivity {
  @JsonProperty("training hours")
  protected int trainingHours;
  @JsonProperty("number of people")
  protected int numberOfPeople;

}
