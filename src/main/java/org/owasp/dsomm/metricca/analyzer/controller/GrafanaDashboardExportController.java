package org.owasp.dsomm.metricca.analyzer.controller;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.skeleton.SkeletonActivity;
import org.owasp.dsomm.metricca.analyzer.grafana.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
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

  @Value("${metricCA.grafana.baseurl:http://localhost:3000}")
  private String grafanaBaseUrl;

  @Value("${metricCA.grafana.apiKey}")
  private String grafanaApiKey;

  @Value("${metricCA.grafana.timeoutInSeconds:10}")
  private Integer grafanaApiTimeoutInSeconds;

  @RequestMapping(value = "/dashboard/overview", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String getOverviewDashboard() throws Exception {
    return overviewDashboard.getDashboard(getPanelConfigurations().values());
  }

  @RequestMapping(value = "/dashboard/overview/push", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String pushOverviewDashboard() throws IOException {
    String status = "{\"status\": \"error\"}";
    try {
      String dashboardString = overviewDashboard.getDashboard(getPanelConfigurations().values());
      GrafanaDashboardCreator grafanaDashboardCreator = new GrafanaDashboardCreator(grafanaBaseUrl, grafanaApiKey, dashboardString, grafanaApiTimeoutInSeconds);
      if (grafanaDashboardCreator.pushDashboard()) {
        status = "{\"status\": \"pushed\", \"dashboard\": \"" + dashboardString + "\"}";
      } else {
        status = "{\"status\": \"error\", \"dashboard\": \"" + dashboardString + "\"}";
      }

    } catch (Exception e) {
      logger.error("Error pushing overview dashboard", e);
      status = "{\"status\": \"error\"}";
    }
    return status;
  }

  private Map<String, PanelConfiguration> getPanelConfigurations() throws GitAPIException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    Map<String, PanelConfiguration> panelConfigurations = new HashMap<String, PanelConfiguration>();
    for (SkeletonActivity activity : ApplicationDirector.getSkeletonActivities()) {
      for (PanelConfiguration panelConfiguration : activity.getPanelConfigurations()) {
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


  @RequestMapping(value = "/dashboard/team", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String getTeamDashboard() throws Exception {
    Map<String, PanelConfiguration> panelConfigurations = new HashMap<String, PanelConfiguration>();
    for (SkeletonActivity skeletonActivity : ApplicationDirector.getSkeletonActivities()) {
      for (PanelConfiguration panelConfiguration : skeletonActivity.getPanelConfigurations()) {
        Map<String, PanelConfiguration> fetchedPanelConfigurations = PanelFactory.getPanelsForLevels(panelConfiguration, skeletonActivity);
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
    return teamDashboard.getDashboard(panelConfigurations.values());
  }
}