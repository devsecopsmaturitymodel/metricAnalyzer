package org.owasp.dsomm.metricca.analyzer.grafana;


import freemarker.template.TemplateException;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
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

  @Override
  protected Collection<String> getPanels(Collection<PanelConfiguration> panelConfigurations) throws TemplateException, IOException {
    PanelConfiguration panelConfiguration = new PanelConfiguration("overview", "overview", "team/${team}/application/${application}/entriesWithTeam", "Matrix Overview of Implementation Level");
    Collection<String> panels = super.getPanels(panelConfigurations);

    ArrayList newPanelConfigs = new ArrayList();
    newPanelConfigs.add(panelConfiguration);
    panels.addAll(super.getPanels(newPanelConfigs));
    return panels;
  }
}

