package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date;

import java.util.List;

public class SecurityTrainingActivity extends Activity {
  @JsonProperty("training hours")
  protected int trainingHours;
  @JsonProperty("number of people")
  protected int numberOfPeople;


  @Override
  public List<Date> getDateComponents() {
    return null; //TODO
  }
}
