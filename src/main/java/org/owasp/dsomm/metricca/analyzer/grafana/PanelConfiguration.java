package org.owasp.dsomm.metricca.analyzer.grafana;

public class PanelConfiguration {
  private String title;
  private String type;
  private String url;

  public PanelConfiguration(String title, String type, String url) {
    this.title = title;
    this.type = type;
    this.url = url;
  }

  public String getTitle() {
    return title;
  }

  public String getType() {
    return type;
  }

  public String getUrl() {
    return url;
  }

}
