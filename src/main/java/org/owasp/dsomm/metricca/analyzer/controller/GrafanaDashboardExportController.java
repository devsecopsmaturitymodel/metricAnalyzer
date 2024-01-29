package org.owasp.dsomm.metricca.analyzer.controller;

import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.grafana.GrafanaDashboardCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;

@Controller
public class GrafanaDashboardExportController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaDashboardExportController.class);

  @Autowired
  private ApplicationDirector applicationDirector;

  @Autowired
  private GrafanaDashboardCreator grafanaDashboardCreator;


  @RequestMapping(value = "/dashboard/{type}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String getOverviewDashboard(@PathVariable String type) throws Exception {
    HashMap<String, String> dashboards = grafanaDashboardCreator.getDashboards();
    if(dashboards.containsKey(type)) {
      return dashboards.get(type);
    } else {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Dashboard not found"
      );
    }
  }

  @RequestMapping(value = "/dashboards/push", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String pushOverviewDashboard() throws IOException {
    String status = "{\"status\": \"error\"}";
    try {
      if (grafanaDashboardCreator.pushDashboards()) {
        status = "{\"status\": \"pushed\"}";
      } else {
        status = "{\"status\": \"error\"}";
      }

    } catch (Exception e) {
      logger.error("Error pushing overview dashboard", e);
      status = "{\"status\": \"error\"}";
    }
    return status;
  }


  @RequestMapping(value = "/dashboard/team", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String getTeamDashboard() throws Exception {
    return grafanaDashboardCreator.getDashboards().get("team");

//    String dashboardType = "team";
//    Map<String, PanelConfiguration> panelConfigurations = new HashMap<String, PanelConfiguration>();
//    for (SkeletonActivity skeletonActivity : ApplicationDirector.getSkeletonActivities()) {
//      for (PanelConfiguration panelConfiguration : skeletonActivity.getPanelConfigurations(dashboardType)) {
//        Map<String, PanelConfiguration> fetchedPanelConfigurations = PanelFactory.getPanelsForLevels(panelConfiguration, skeletonActivity);
//        for (PanelConfiguration fetchedPanelConfiguration : fetchedPanelConfigurations.values()) {
//          if (!panelConfigurations.containsKey(fetchedPanelConfiguration.getTitle())) {
//            panelConfigurations.put(fetchedPanelConfiguration.getTitle(), fetchedPanelConfiguration);
//          }
//        }
//      }
//    }
//    for (PanelConfiguration panelConfiguration : panelConfigurations.values()) {
//      logger.info("panelConfiguration: " + panelConfiguration.getTitle());
//    }
//    return teamDashboard.getDashboard(panelConfigurations.values());
  }
}