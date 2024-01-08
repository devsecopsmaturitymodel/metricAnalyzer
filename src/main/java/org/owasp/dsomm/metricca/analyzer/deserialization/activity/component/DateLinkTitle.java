package org.owasp.dsomm.metricca.analyzer.deserialization.activity.component;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DateLinkTitle extends Date {
  @JsonProperty("title")
  protected String title;

  @JsonProperty("links")
  protected List<Link> links;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }
}
