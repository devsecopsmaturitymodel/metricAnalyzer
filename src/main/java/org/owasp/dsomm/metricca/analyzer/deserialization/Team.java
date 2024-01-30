package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Team {
  @JsonProperty("name")
  private String name;

  @JsonProperty("applications")
  private List<String> applications;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getApplications() {
    if(applications == null) {
      applications = new ArrayList<>();
    }
    return applications;
  }

  public void setApplications(List<String> applications) {
    this.applications = applications;
  }
}
