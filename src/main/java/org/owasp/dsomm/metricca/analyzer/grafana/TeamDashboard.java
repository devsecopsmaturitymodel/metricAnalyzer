package org.owasp.dsomm.metricca.analyzer.grafana;


import freemarker.template.TemplateException;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Configuration
public class TeamDashboard extends GenericDashboard {
  protected String dashboardType = "team";

  public TeamDashboard() {
    super();
  }

  @Override
  public String getDashboardType() {
    return dashboardType;
  }

  @Override
  protected Map<String, Object> getDashboardTemplateReplacements(Collection<PanelConfiguration> panels) throws TemplateException, IOException {
    Map<String, Object> replacements = super.getDashboardTemplateReplacements(panels);
    replacements.put("apiUrl", apiBaseUrl);
    replacements.put("infinityDatasourceId", infinityDatasourceId);
    return replacements;
  }


}

