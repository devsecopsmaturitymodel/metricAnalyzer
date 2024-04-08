package org.owasp.dsomm.metricca.analyzer.controller;

import org.owasp.dsomm.metricca.analyzer.controller.dto.FlattenDate;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.SecurityTrainingActivity;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.DatePeriodHoursAndPeople;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class GrafanaOverviewDashboardController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaOverviewDashboardController.class);

  @Autowired
  private ApplicationDirector applicationDirector;

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

  /* Grafana type: counter */
  @RequestMapping(value = "/activity/{activityName}/level/{level}/count", method = RequestMethod.GET)
  @ResponseBody
  public HashMap<String, Integer> getActivitiesWithCount(@PathVariable String activityName, @PathVariable String level) throws Exception {
    HashMap<String, Integer> activityMap = new HashMap<String, Integer>();
    for (Application application : applicationDirector.getApplications()) {
      for (Activity activity : application.getActivities(activityName)) {
        logger.info("activity: " + activity.getName());
        activityMap.put(application.getTeam(), activity.getThresholdDatePeriodMap().get(level).getThresholdValue());
      }
    }
    return activityMap;
  }

  /**
   * Grafana type: timeseries-flatdate
   */
  @RequestMapping(value = "/activity/{activityName}/level/{level}/flatdate", method = RequestMethod.GET)
  @ResponseBody
  public Collection<FlattenDate> getActivitiesFlat(@PathVariable String activityName, @PathVariable String level) throws Exception {
    return applicationDirector.getActivitiesFlat(activityName, level);
  }

  /**
   * Grafana type: hours-and-people
   */
  @RequestMapping(value = "/activity/{activityName}/level/{level}/hoursAndPeople", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, List<DatePeriodHoursAndPeople>> getActivitiesHoursAndPeople(@PathVariable String activityName, @PathVariable String level) throws Exception {
    Map<String, List<DatePeriodHoursAndPeople>> allActivityDates = new HashMap<>();
    for (Application application : applicationDirector.getApplications()) {
      for (Activity activity : application.getActivities(activityName)) {
        logger.info("activity: " + activity.getName());
        if (!(activity instanceof SecurityTrainingActivity)) {
          throw new Exception("SecurityTrainingActivity not implemented yet");
        }

        for (DatePeriodHoursAndPeople datePeriodHoursAndPeople : ((SecurityTrainingActivity) activity).getLearningTimePerDate()) {
          logger.info("datePeriodHoursAndPeople: " + datePeriodHoursAndPeople.getDate());
          FlattenDate flattenDate = new FlattenDate(datePeriodHoursAndPeople.getDate());
          flattenDate.addDynamicField(application.getTeam(), datePeriodHoursAndPeople.getHours());
        }

        allActivityDates.put(application.getTeam(), ((SecurityTrainingActivity) activity).getLearningTimePerDate());
      }
    }
    return allActivityDates;
  }
}
