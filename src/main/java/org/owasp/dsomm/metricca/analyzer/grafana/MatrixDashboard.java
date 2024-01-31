package org.owasp.dsomm.metricca.analyzer.grafana;


import freemarker.template.TemplateException;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Configuration
public class MatrixDashboard extends GenericDashboard {
  protected String dashboardType = "matrix";

  public MatrixDashboard() {
    super();
  }

  @Override
  public String getDashboardType() {
    return dashboardType;
  }
}

