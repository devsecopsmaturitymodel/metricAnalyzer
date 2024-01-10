package org.owasp.dsomm.metricca.analyzer.deserialization.skeleton;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.threshold.Threshold;
import org.owasp.dsomm.metricca.analyzer.grafana.PanelConfiguration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class SkeletonActivity {
  @JsonProperty("activity names")
  protected List<String> activityNames;

  @JsonProperty("thresholds")
  protected List<Threshold> thresholds = new ArrayList<Threshold>();

  @JsonProperty("class name")
  protected String className;

  @JsonProperty("kind")
  protected String kind;

  @JsonProperty("grafana panel type")
  protected String grafanaPanelType;

  public static String urlEncode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
  }

  @JsonIgnore
  public List<PanelConfiguration> getPanelConfigurations() {
    List<PanelConfiguration> panelConfigurations = new ArrayList<PanelConfiguration>();
    for (String name : activityNames) {
      panelConfigurations.add(new PanelConfiguration(name, grafanaPanelType, "activity/" + urlEncode(name), ""));
    }
    return panelConfigurations;
  }

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

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

}
