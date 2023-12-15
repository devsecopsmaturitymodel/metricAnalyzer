package org.owasp.dsomm.metricca.analyzer.model.threshold;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Thresholds {

  @JsonProperty("thresholds")
  private ArrayList<Threshold> thresholds;

  public ArrayList<Threshold> getThresholds() {
    return thresholds;
  }
}
