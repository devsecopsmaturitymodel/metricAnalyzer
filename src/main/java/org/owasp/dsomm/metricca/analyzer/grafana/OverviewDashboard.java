package org.owasp.dsomm.metricca.analyzer.grafana;


import org.springframework.context.annotation.Configuration;

@Configuration
public class OverviewDashboard extends GenericDashboard {
  protected String dashboardType = "overview";

  public OverviewDashboard() {
    super();
  }

  @Override
  public String getDashboardType() {
    return dashboardType;
  }
}
