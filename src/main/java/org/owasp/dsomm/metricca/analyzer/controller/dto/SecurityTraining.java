package org.owasp.dsomm.metricca.analyzer.controller.dto;

import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.Date;

public class SecurityTraining {
  private final String team;
  private final int hours;

  private final Date date;

  public SecurityTraining(String team, int hours, Date date) {
    this.team = team;
    this.hours = hours;
    this.date = date;
  }
}
