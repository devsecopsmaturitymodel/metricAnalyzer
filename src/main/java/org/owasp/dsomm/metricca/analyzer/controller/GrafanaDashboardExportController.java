package org.owasp.dsomm.metricca.analyzer.controller;

import com.appnexus.grafana.client.models.Dashboard;
import com.appnexus.grafana.client.models.GrafanaDashboard;
import com.appnexus.grafana.configuration.GrafanaConfiguration;
import com.appnexus.grafana.exceptions.GrafanaException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.grafana.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.appnexus.grafana.client.GrafanaClient;

import java.io.IOException;
import java.net.URL;
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
  @RequestMapping(value = "/dashboard/overview/push-lib", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String pushOverviewDashboardWithLib() throws GrafanaException, IOException {
    //Setup the client
    GrafanaConfiguration grafanaConfiguration =
        new GrafanaConfiguration().host(grafanaBaseUrl).apiKey("Bearer " + grafanaApiKey);
    GrafanaClient grafanaClient = new GrafanaClient(grafanaConfiguration);

//Setup the dashboard
    String DASHBOARD_NAME = "testdashboard";

    Dashboard dashboard = new Dashboard()
        .title(DASHBOARD_NAME)
        .version(1);

    GrafanaDashboard grafanaDashboard = new GrafanaDashboard().dashboard(dashboard);

//Make API calls
    try {
      grafanaClient.deleteDashboard(DASHBOARD_NAME);
    } catch (GrafanaException e) {
      logger.error("Error deleteDashboard", e);
    }
    try {
      grafanaClient.createDashboard(grafanaDashboard);
    } catch (GrafanaException e) {
      logger.error("Error creating dashboard", e);
    }
    try {
      grafanaClient.getDashboard(DASHBOARD_NAME);
    } catch (GrafanaException e) {
      logger.error("Error getting dashboard", e);
    }
    return "{\"status\": \"pushed?\"}";
  }
  @RequestMapping(value = "/dashboard/overview/push", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String pushOverviewDashboard() throws GrafanaException, IOException {
//    GrafanaConfiguration grafanaConfiguration =
//        new GrafanaConfiguration().host(grafanaBaseUrl).apiKey("Bearer " + grafanaApiKey);
//    GrafanaClient grafanaClient = new GrafanaClient(grafanaConfiguration);
////Make API calls
//    //Setup the dashboard
//
//
//    Dashboard dashboard = new Dashboard()
//        .title(overviewDashboard.getTitle())
//        .version(1);
//
//    GrafanaDashboard grafanaDashboard = new GrafanaDashboard().dashboard(dashboard);
//    try {
//      grafanaClient.createDashboard(grafanaDashboard);
//      logger.info("Dashboard created" + overviewDashboard.getTitle());
//    } catch (GrafanaException e) {
//      logger.error("Error creating dashboard", e);
//    }

    String status = "{\"status\": \"error\"}";
    try {
      String dashboardString = overviewDashboard.getDashboard(getPanelConfigurations().values());
      GrafanaDashboardCreator grafanaDashboardCreator = new GrafanaDashboardCreator(grafanaBaseUrl, grafanaApiKey, dashboardString, grafanaApiTimeoutInSeconds);
      if(grafanaDashboardCreator.pushDashboard()) {
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
    Application application = applicationDirector.getApplications().get(0);
    for (Activity activity : application.getActivities()) {
      PanelConfiguration panelConfiguration = activity.getPanelConfiguration();
      Map<String, PanelConfiguration> fetchedPanelConfigurations = PanelFactory.getPanelsForLevels(panelConfiguration, activity);
      for (PanelConfiguration fetchedPanelConfiguration : fetchedPanelConfigurations.values()) {
        if (!panelConfigurations.containsKey(fetchedPanelConfiguration.getTitle())) {
          panelConfigurations.put(fetchedPanelConfiguration.getTitle(), fetchedPanelConfiguration);
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
    Application application = applicationDirector.getApplications().get(0);
    for (Activity activity : application.getActivities()) {
      PanelConfiguration panelConfiguration = activity.getPanelConfiguration();
      Map<String, PanelConfiguration> fetchedPanelConfigurations = PanelFactory.getPanelsForLevels(panelConfiguration, activity);
      for (PanelConfiguration fetchedPanelConfiguration : fetchedPanelConfigurations.values()) {
        if (!panelConfigurations.containsKey(fetchedPanelConfiguration.getTitle())) {
          panelConfigurations.put(fetchedPanelConfiguration.getTitle(), fetchedPanelConfiguration);
        }
      }
    }
    for (PanelConfiguration panelConfiguration : panelConfigurations.values()) {
      logger.info("panelConfiguration: " + panelConfiguration.getTitle());
    }
    return teamDashboard.getDashboard(panelConfigurations.values());
  }
}