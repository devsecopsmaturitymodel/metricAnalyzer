package org.owasp.dsomm.metricca.analyzer.grafana;

import freemarker.template.TemplateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.SkeletonActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class GrafanaDashboardCreator {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaDashboardCreator.class);

  @Autowired
  private OverviewDashboard overviewDashboard;

  @Autowired
  private TeamDashboard teamDashboard;

  @Autowired
  private MatrixDashboard matrixDashboard;

  @Value("${metricCA.grafana.baseurl:http://localhost:3000}")
  private String grafanaBaseUrl;

  @Value("${metricCA.grafana.apiKey}")
  private String grafanaApiKey;

  @Value("${metricCA.grafana.timeoutInSeconds:10}")
  private Integer grafanaApiTimeoutInSeconds;


  public HashMap<String, String> getDashboards() throws GitAPIException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, TemplateException {
    HashMap<String, String> dashboards = new HashMap<String, String>();
    dashboards.put("overview", overviewDashboard.getDashboard(getPanelConfigurations("overview").values()));
    dashboards.put("team", teamDashboard.getDashboard(getPanelConfigurations("team").values()));
    dashboards.put("matrix", matrixDashboard.getDashboard(getMatrixPanel().values()));
    return dashboards;
  }

  public boolean pushDashboards() throws Exception {
    boolean status = true;
    for (Map.Entry<String, String> dashboard : getDashboards().entrySet()) {
      logger.info("Pushing dashboard: " + dashboard.getKey());
      GrafanaDashboardPusher grafanaDashboardPusher = new GrafanaDashboardPusher(grafanaBaseUrl, grafanaApiKey, dashboard.getValue(), grafanaApiTimeoutInSeconds);
      if (!grafanaDashboardPusher.pushDashboard()) {
        status = false;
      }
    }
    return status;
  }

  private Map<String, PanelConfiguration> getPanelConfigurations(String dashboardType) throws GitAPIException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    Map<String, PanelConfiguration> panelConfigurations = new HashMap<String, PanelConfiguration>();
    for (SkeletonActivity activity : ApplicationDirector.getSkeletonActivities()) {
      for (PanelConfiguration panelConfiguration : activity.getPanelConfigurations(dashboardType)) {
        Map<String, PanelConfiguration> fetchedPanelConfigurations = PanelFactory.getPanelsForLevels(panelConfiguration, activity);
        for (PanelConfiguration fetchedPanelConfiguration : fetchedPanelConfigurations.values()) {
          if (!panelConfigurations.containsKey(fetchedPanelConfiguration.getTitle())) {
            panelConfigurations.put(fetchedPanelConfiguration.getTitle(), fetchedPanelConfiguration);
          }
        }
      }
    }
    for (PanelConfiguration panelConfiguration : panelConfigurations.values()) {
      logger.info("panelConfiguration: " + panelConfiguration.getTitle());
    }
    return panelConfigurations;
  }

  private Map<String, PanelConfiguration> getMatrixPanel() {
    Map<String, PanelConfiguration> panelConfigurations = new HashMap<String, PanelConfiguration>();
    PanelConfiguration panelConfiguration = new PanelConfiguration("Matrix", "table-overview", "matrix/overview", "Overview of all applications");
    panelConfigurations.put(panelConfiguration.getTitle(), panelConfiguration);
    return panelConfigurations;
  }
}