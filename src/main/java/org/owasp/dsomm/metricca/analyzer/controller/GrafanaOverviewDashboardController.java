package org.owasp.dsomm.metricca.analyzer.controller;

import org.owasp.dsomm.metricca.analyzer.grafana.OverviewDashboard;
import org.owasp.dsomm.metricca.analyzer.grafana.PanelConfiguration;
import org.owasp.dsomm.metricca.analyzer.model.Activity;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Target;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Threshold;
import org.owasp.dsomm.metricca.analyzer.model.threshold.target.CountTarget;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class GrafanaOverviewDashboardController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaOverviewDashboardController.class);

  @Autowired
  private ApplicationDirector applicationDirector;

  @Autowired
  private OverviewDashboard overviewDashboard;

  @RequestMapping(value = "/dashboard/overview", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String getTeamDashboard() throws Exception {
    PanelConfiguration panelConfiguration = new PanelConfiguration("Team Dashboard", "timeseries", "teamdashboard");
    ArrayList<PanelConfiguration> panelConfigurations = new ArrayList<PanelConfiguration>();
    panelConfigurations.add(panelConfiguration);
    panelConfigurations.add(new PanelConfiguration("zweites Panel", "timeseries", "blabl"));

    return overviewDashboard.getDashboard(panelConfigurations);
  }

  @RequestMapping(value = "/activity/{activityName}", method = RequestMethod.GET)
  @ResponseBody
  public HashMap<String, Activity> getActivitiesSimpleNoDate(@PathVariable String activityName) throws Exception {
    HashMap<String, Activity> activityMap = new HashMap<String, Activity>();
    for (Application application : applicationDirector.getApplications()) {
      for (Activity activity : application.getActivities(activityName)) {
        if (activity.getName().equals(activityName)) {
          activityMap.put(application.getTeam(), activity);
        }
      }
    }
    return activityMap;
  }

  @RequestMapping(value = "/activity/{activityNameWithoutLevel}/level/{level}/count", method = RequestMethod.GET)
  @ResponseBody
  public HashMap<String, Integer> getActivitiesWithCount(@PathVariable String activityName, @PathVariable String level) throws Exception {
    HashMap<String, Integer> activityMap = new HashMap<String, Integer>();
    for (Application application : applicationDirector.getApplications()) {
      for (Activity activity : application.getActivities(activityName, level)) {
        logger.info("activity: " + activity.getName());
        for (Threshold threshold : activity.getThresholds().getThresholds()) {
          ArrayList<Component> components = activity.getIntComponents();
          threshold.getThresholdReached(components);
          threshold.getThresholdReached(components);
          for (Target target : threshold.getTargets()) {
            if (target instanceof CountTarget countTarget) {
              activityMap.put(application.getTeam(), countTarget.getCount());
            }
          }
        }
      }
    }
    return activityMap;
  }

}