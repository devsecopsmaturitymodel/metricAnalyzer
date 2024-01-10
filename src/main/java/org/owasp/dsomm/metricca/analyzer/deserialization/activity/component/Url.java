package org.owasp.dsomm.metricca.analyzer.deserialization.activity.component;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

public class Url extends Date {
  @JsonProperty("url")
  protected URL url;

  public URL getUrl() {
    return url;
  }

  public void setUrl(URL url) {
    this.url = url;
  }
}
