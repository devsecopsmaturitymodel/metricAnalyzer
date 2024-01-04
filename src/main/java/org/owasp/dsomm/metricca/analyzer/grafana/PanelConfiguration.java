package org.owasp.dsomm.metricca.analyzer.grafana;

public class PanelConfiguration {
  private final String type;
  private String title;
  private String url;

  private String description;

  public PanelConfiguration(String title, String type, String url, String description) {
    this.title = title;
    this.type = type;
    this.url = url;
    this.description = description;
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDescription() {
    return description;
  }
}
