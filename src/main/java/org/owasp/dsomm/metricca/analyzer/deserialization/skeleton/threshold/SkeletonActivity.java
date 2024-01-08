package org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


public class SkeletonActivity {
  @JsonProperty("activity names")
  protected List<String> activityNames;

  @JsonProperty("thresholds")
  protected List<Threshold> thresholds = new ArrayList<Threshold>();

  @JsonProperty("class name")
  protected String className;

  @JsonProperty("grafana panel type")
  protected String grafanaPanelType;

  public List<Threshold> getThresholds() {
    return thresholds;
  }

  public void setThresholds(List<Threshold> thresholds) {
    this.thresholds = thresholds;
  }

  public List<String> getActivityNames() {
    return activityNames;
  }

  public void setActivityNames(List<String> activityNames) {
    this.activityNames = activityNames;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getGrafanaPanelType() {
    return grafanaPanelType;
  }

  public void setGrafanaPanelType(String grafanaPanelType) {
    this.grafanaPanelType = grafanaPanelType;
  }
}