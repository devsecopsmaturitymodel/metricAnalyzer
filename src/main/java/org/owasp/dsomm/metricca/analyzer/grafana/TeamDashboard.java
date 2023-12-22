package org.owasp.dsomm.metricca.analyzer.grafana;


import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class TeamDashboard extends GenericDashboard {
  protected String dashboardType = "team";

  @Value("${metricCA.grafana.datasource.uuid:cfffcb9a-60ce-4250-97d5-4021a21405ff}")
  protected String datasourceUuid;

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
    return replacements;
  }
}
