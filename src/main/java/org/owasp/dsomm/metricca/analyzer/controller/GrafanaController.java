package org.owasp.dsomm.metricca.analyzer.controller;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.owasp.dsomm.metricca.analyzer.grafana.OverviewDashboard;
import org.owasp.dsomm.metricca.analyzer.grafana.PanelConfiguration;
import org.owasp.dsomm.metricca.analyzer.model.FlattenDate;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Activity;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.DateComponent;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.components.DatePeriodComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
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

  @RequestMapping(value = "/activity/{activityName}/nodate", method = RequestMethod.GET)
  @ResponseBody
  public HashMap<String,Activity> getActivitiesSimpleNoDate(@PathVariable String activityName) throws Exception {
    HashMap<String,Activity> activityMap = new HashMap<String,Activity>();
    for (Application application : applicationDirector.getApplications()) {
      for (Activity activity : application.getActivities(activityName)) {
        if (activity.getName().equals(activityName)) {
          activityMap.put(application.getTeam(), activity);
        }
      }
    }
    return activityMap;
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

  @RequestMapping(value = "/activity/{activityName}/date", method = RequestMethod.GET)
  @ResponseBody
  public Collection<FlattenDate> getActivitiesFlat(@PathVariable String activityName) throws Exception {
    Collection<FlattenDate> flattenedActivitiesToReturn = new ArrayList<FlattenDate>();
    Collection<Date> datesFromActivities = applicationDirector.getDatesFromActivities(activityName);
    for (Date date : datesFromActivities) {
      FlattenDate flattenDate = new FlattenDate(date);
      for (Application application : applicationDirector.getApplications()) {
        for (Activity activity : application.getActivities(activityName)) {
          boolean value = false;

          DateComponent dateComponent = activity.getMatchingDatePeriodComponent(date);
          logger.debug("dateComponent: " + dateComponent);
          if (dateComponent != null) {
            logger.debug("date == dateComponent.getValue()" + dateComponent.getValue());
            if (dateComponent instanceof DatePeriodComponent) {
              logger.debug("Found activity: " + activity.getName() + " in application: " + application.getApplication() + " with datePeriodComponent: " + dateComponent + " team: " + application.getTeam());
              value = ((DatePeriodComponent) dateComponent).isActive();
            } else {
              value = true;
            }
          } else {
            dateComponent = activity.getClosestBeforeDatePeriodComponent(date);
            if (dateComponent != null) {
              value = ((DatePeriodComponent) dateComponent).isActive();
              logger.debug("Found activity without matching date component: " + activity.getName() + " in application: " + application.getApplication() + " with closest dateComponent: " + dateComponent + "for date: " + date + " team: " + application.getTeam() + "and value: " + value);
            }
          }
          flattenDate.addDynamicField(application.getTeam() + "-" + application.getApplication(), value);
        }
      }
      flattenedActivitiesToReturn.add(flattenDate);
    }
    logger.debug("activitiesToReturn: " + flattenedActivitiesToReturn);
    return flattenedActivitiesToReturn;
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

  @RequestMapping(value = "/applications", method = RequestMethod.GET)
  @ResponseBody
  public Collection<String> getApplicationIds() throws IOException, GitAPIException {
    Set<String> applicationIds = new HashSet<>();
    for (Application application : applicationDirector.getApplications()) {
      applicationIds.add(application.getApplication());
    }
    return applicationIds;
  }

  @RequestMapping(value = "/teams", method = RequestMethod.GET)
  @ResponseBody
  public Collection<String> getTeams() throws IOException, GitAPIException {
    Set<String> teams = new HashSet<>();
    for (Application application : applicationDirector.getApplications()) {
      teams.add(application.getTeam());
    }
    return teams;
  }
}