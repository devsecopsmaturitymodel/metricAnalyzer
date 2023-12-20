package org.owasp.dsomm.metricca.analyzer.deserialization.activity.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.Period;

import java.util.List;

public class LinkAndTitle {
  @JsonProperty("title")
  private String title;

  @JsonProperty("links")
  private List<Link> links;
}
