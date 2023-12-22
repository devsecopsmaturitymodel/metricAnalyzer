package org.owasp.dsomm.metricca.analyzer.controller;

import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.grafana.OverviewDashboard;
import org.owasp.dsomm.metricca.analyzer.controller.dto.FlattenDate;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class GrafanaController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaController.class);

  @Autowired
  private ApplicationDirector applicationDirector;

  @Autowired
  private OverviewDashboard overviewDashboard;

  @RequestMapping(value = "/activities", method = RequestMethod.GET)
  @ResponseBody
  public Collection<Activity> getActivities() throws Exception {
    Collection<Activity> activities = new ArrayList<Activity>();
    for (Application application : applicationDirector.getApplications()) {
      activities.addAll(application.getActivities());
    }
    return activities;
  }



  @RequestMapping(value = "/", method = RequestMethod.GET)
  @ResponseBody
  public Collection<Application> getApplications() throws Exception {
    return applicationDirector.getApplications();
  }

  @RequestMapping(value = "/team/{teamName}/applications", method = RequestMethod.GET)
  @ResponseBody
  public Collection<Application> getTeamApplications(@PathVariable String teamName) throws Exception {
    Collection<Application> applicationsToReturn = new ArrayList<Application>();
    for (Application application : applicationDirector.getApplications()) {
      if (application.getTeam().equals(teamName)) {
        applicationsToReturn.add(application);
      }
    }
    return applicationsToReturn;
  }

  @RequestMapping(value = "/team/{teamName}/applicationIds", method = RequestMethod.GET)
  @ResponseBody
  public Collection<String> getTeamApplicationIds(@PathVariable String teamName) throws Exception {
    Collection<String> applicationsToReturn = new ArrayList<String>();
    for (Application application : applicationDirector.getApplications()) {
      if (application.getTeam().equals(teamName)) {
        applicationsToReturn.add(application.getApplication());
      }
    }
    return applicationsToReturn;
  }

  @RequestMapping(value = "/activity/{activityName}/simple", method = RequestMethod.GET)
  @ResponseBody
  public Collection<FlattenDate> getActivitiesFlatSimple(@PathVariable String activityName) throws Exception {
    return applicationDirector.getActivitiesFlat(activityName);
  }

  @RequestMapping(value = "/team/{teamName}/application/{application}/activity/{activityName}/simple", method = RequestMethod.GET)
  @ResponseBody
  public Collection<FlattenDate> getActivitiesPerTeamFlatSimple(@PathVariable String teamName, @PathVariable String application, @PathVariable String activityName) throws Exception {
    return applicationDirector.getActivitiesPerTeamAndApplicationFlat(teamName, application, activityName);
  }



  @RequestMapping(value = "/team/{teamName}/application/{applicationId}/activity/{activityName}", method = RequestMethod.GET)
  @ResponseBody
  public Collection<Activity> getTeamActivity(@PathVariable String teamName, @PathVariable String applicationId, @PathVariable String activityName) throws Exception {
    Collection<Activity> activitiesToReturn = new ArrayList<Activity>();
    for (Application application : applicationDirector.getApplications()) {
      if (application.getTeam().equals(teamName)) {
        if (application.getApplication().equals(applicationId)) {
          for (Activity activity : application.getActivities()) {
            if (activity.getName().equals(activityName)) {
              logger.debug("Found activity: " + activity.getName() + " in application: " + application.getApplication());
              activitiesToReturn.add(activity);
            }
          }
        }
      }
    }
    logger.debug("activitiesToReturn: " + activitiesToReturn);
    return activitiesToReturn;
  }




}