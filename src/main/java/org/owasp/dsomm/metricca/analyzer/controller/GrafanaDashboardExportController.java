package org.owasp.dsomm.metricca.analyzer.controller;

import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.grafana.OverviewDashboard;
import org.owasp.dsomm.metricca.analyzer.grafana.PanelConfiguration;
import org.owasp.dsomm.metricca.analyzer.grafana.PanelFactory;
import org.owasp.dsomm.metricca.analyzer.grafana.TeamDashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GrafanaDashboardExportController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaDashboardExportController.class);

  @Autowired
  private ApplicationDirector applicationDirector;

  @Autowired
  private TeamDashboard teamDashboard;
  @Autowired
  private OverviewDashboard overviewDashboard;

  @RequestMapping(value = "/dashboard/overview", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String getOverviewDashboard() throws Exception {
    Map<String, PanelConfiguration> panelConfigurations = new HashMap<String, PanelConfiguration>();
    Application application = applicationDirector.getApplications().get(0);
//    for() {
    for (Activity activity : application.getActivities()) {
      PanelConfiguration panelConfiguration = activity.getPanelConfiguration();
      Map<String, PanelConfiguration> fetchedPanelConfigurations = PanelFactory.getPanelsForLevels(panelConfiguration, activity);
      for (PanelConfiguration fetchedPanelConfiguration : fetchedPanelConfigurations.values()) {
        if (!panelConfigurations.containsKey(fetchedPanelConfiguration.getTitle())) {
          panelConfigurations.put(fetchedPanelConfiguration.getTitle(), fetchedPanelConfiguration);
        }
      }
    }
//    }
    for (PanelConfiguration panelConfiguration : panelConfigurations.values()) {
      logger.info("panelConfiguration: " + panelConfiguration.getTitle());
    }
    return overviewDashboard.getDashboard(panelConfigurations.values());
  }
  @RequestMapping(value = "/dashboard/team", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String getTeamDashboard() throws Exception {
    Map<String, PanelConfiguration> panelConfigurations = new HashMap<String, PanelConfiguration>();
    Application application = applicationDirector.getApplications().get(0);
//    for() {
    for (Activity activity : application.getActivities()) {
      PanelConfiguration panelConfiguration = activity.getPanelConfiguration();
      Map<String, PanelConfiguration> fetchedPanelConfigurations = PanelFactory.getPanelsForLevels(panelConfiguration, activity);
      for (PanelConfiguration fetchedPanelConfiguration : fetchedPanelConfigurations.values()) {
        if (!panelConfigurations.containsKey(fetchedPanelConfiguration.getTitle())) {
          panelConfigurations.put(fetchedPanelConfiguration.getTitle(), fetchedPanelConfiguration);
        }
      }
    }
//    }
    for (PanelConfiguration panelConfiguration : panelConfigurations.values()) {
      logger.info("panelConfiguration: " + panelConfiguration.getTitle());
    }
    return teamDashboard.getDashboard(panelConfigurations.values());
  }
}