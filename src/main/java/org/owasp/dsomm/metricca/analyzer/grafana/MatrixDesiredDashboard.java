package org.owasp.dsomm.metricca.analyzer.grafana;


import org.springframework.context.annotation.Configuration;

@Configuration
public class MatrixDesiredDashboard extends GenericDashboard {
  protected String dashboardType = "matrix-desired";

  public MatrixDesiredDashboard() {
    super();
  }

  @Override
  public String getDashboardType() {
    return dashboardType;
  }
}

