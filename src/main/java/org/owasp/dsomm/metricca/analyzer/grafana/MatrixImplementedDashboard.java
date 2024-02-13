package org.owasp.dsomm.metricca.analyzer.grafana;


import org.springframework.context.annotation.Configuration;

@Configuration
public class MatrixImplementedDashboard extends GenericDashboard {
  protected String dashboardType = "matrix-implemented";

  public MatrixImplementedDashboard() {
    super();
  }

  @Override
  public String getDashboardType() {
    return dashboardType;
  }
}

