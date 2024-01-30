package org.owasp.dsomm.metricca.analyzer.grafana;

import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.SkeletonActivity;

public class PanelConfiguration {
  private final String type;
  private final String description;
  private String title;
  private String urlPath;

  private String dashboardType;

  public PanelConfiguration(String title, String type, String urlPath, String description) {
    this.title = title;
    this.type = type;
    this.urlPath = urlPath;
    this.description = description;
    this.dashboardType = "overview";
  }

  public PanelConfiguration(String title, String type, String urlPath, String description, String dashboardType) {
    this.title = title;
    this.type = type;
    this.urlPath = urlPath;
    this.description = description;
    this.dashboardType = dashboardType;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getType() {
    return type;
  }

  public String getUrlPath() {
    return urlPath;
  }

  public void setUrlPath(String urlPath) {
    this.urlPath = urlPath;
  }

  public String getDescription() {
    return description;
  }

  public String getTitleUrlEncoded() {
    return SkeletonActivity.urlEncode(title);
  }

  public String getDashboardType() {
    return dashboardType;
  }

  public void setDashboardType(String dashboardType) {
    this.dashboardType = dashboardType;
  }
}
