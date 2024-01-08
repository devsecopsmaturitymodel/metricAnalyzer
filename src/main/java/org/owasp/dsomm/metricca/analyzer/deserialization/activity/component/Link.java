package org.owasp.dsomm.metricca.analyzer.deserialization.activity.component;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

public class Link {

  @JsonProperty("title")
  private String title;

  @JsonProperty("url")
  private URL url;
}
