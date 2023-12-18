package org.owasp.dsomm.metricca.analyzer.deserialization.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Threshold;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Thresholds;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class Activity {
  @JsonProperty("name")
  protected String name;

  @JsonProperty("thresholds")
  protected List<Threshold> thresholds = new ArrayList<Threshold>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Threshold> getThresholds() {
    return thresholds;
  }

  public void setThresholds(List<Threshold> thresholds) {
    this.thresholds = thresholds;
  }
}
