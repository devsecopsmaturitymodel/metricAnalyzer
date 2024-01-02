package org.owasp.dsomm.metricca.analyzer.controller;

import org.owasp.dsomm.metricca.analyzer.controller.dto.FlattenDate;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.DatePeriod;
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
    Collection<FlattenDate> flattenedActivitiesToReturn = new ArrayList<FlattenDate>();
    List<Date> datesFromActivities = applicationDirector.getStartAndEndDateFromActivities(activityName, level);
    logger.info("datesFromActivities: " + datesFromActivities);
    for (Date date : datesFromActivities) {
      FlattenDate flattenDate = new FlattenDate(date);
      for (Application application : applicationDirector.getApplications()) {
        for (Activity activity : application.getActivities(activityName)) {
          boolean value = false;
          org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.DatePeriod dateComponent = null;
          if (activity.getThresholdDatePeriodMap().get(level) == null) {
            logger.debug("1activity.getThresholdDatePeriodMap().get(level) == null");
          } else {
            if (isDateStartDatePeriod(date, activity, level)) {
              logger.info("isDateAStartDatePeriod " + activityName + " " + level + " " + date + " " + application.getTeam());
              value = true;
            } else if (isDateEndDatePeriod(date, activity, level)) {
              logger.info("isDateAEndDatePeriod " + activityName + " " + level + " " + date);
              dateComponent = activity.getThresholdDatePeriodMap().get(level).getDatePeriodEndForDate(date);
              value = !dateComponent.getShowEndDate();
            } else if (isDateEndDatePeriodEnforced(date, activity, level)) {
              logger.info("isDateAEndDateEnforcedPeriod " + activityName + " " + level + " " + date + " " + application.getTeam());
              dateComponent = activity.getThresholdDatePeriodMap().get(level).getClosestBeforeDatePeriodComponent(date);
              if (dateComponent == null) { //no DatePeriod found, that means it is not implemented (yet)
                value = false;
              } else {
                value = dateComponent.isInPeriod(date);
              }
            } else { // it is a DatePeriod from an other activity
              logger.info("an other activity " + activityName + " " + level + " " + date + " " + application.getTeam());
              dateComponent = activity.getThresholdDatePeriodMap().get(level).getClosestBeforeDatePeriodComponent(date);
              if (dateComponent == null) {
                value = false; //  no DatePeriod found, that means it is not implemented (yet)
              } else {
                value = dateComponent.isInPeriod(date);
              }
            }
          }
          flattenDate.addDynamicField(application.getTeam() + "-" + application.getApplication(), value);
        }
      }
      flattenedActivitiesToReturn.add(flattenDate);
    }
    return flattenedActivitiesToReturn;
  }

  private boolean isDateStartDatePeriod(Date date, Activity activity, String level) {
    DatePeriod dateComponent = activity.getThresholdDatePeriodMap().get(level).getDatePeriodForDate(date);
    return dateComponent != null;
  }

  private boolean isDateEndDatePeriod(Date date, Activity activity, String level) {
    DatePeriod dateComponent = activity.getThresholdDatePeriodMap().get(level).getDatePeriodEndForDate(date);
    return dateComponent != null;
  }

  private boolean isDateEndDatePeriodEnforced(Date date, Activity activity, String level) {
    DatePeriod dateComponent = activity.getThresholdDatePeriodMap().get(level).getDatePeriodEndForDateEnforced(date);
    return dateComponent != null;
  }
}