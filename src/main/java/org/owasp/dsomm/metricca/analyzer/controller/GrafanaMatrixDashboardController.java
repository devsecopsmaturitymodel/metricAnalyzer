package org.owasp.dsomm.metricca.analyzer.controller;

import org.owasp.dsomm.metricca.analyzer.controller.dto.FlattenDate;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;

@Controller
public class GrafanaMatrixDashboardController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaMatrixDashboardController.class);

  @Autowired
  ApplicationDirector applicationDirector;

  @RequestMapping(value = "/matrix/overview/desiredLevel", method = RequestMethod.GET)
  @ResponseBody
  public ArrayList<FlattenDate> getOverviewDashboardDesiredLevel() throws Exception {
    ArrayList<FlattenDate> flattenDates = new ArrayList<>();
    for (Application application : applicationDirector.getApplications()) {
      FlattenDate flattenDate = new FlattenDate(new Date());
      flattenDate.addDynamicField("date", new Date());
      flattenDate.addDynamicField("Application", application.getTeam() + "-" + application.getName() + ", target: " + application.getDesiredLevel());
      for (Activity activity : application.getActivities()) {
        String desiredLevel = application.getDesiredLevel();
        if (application.getDesiredLevel() == null) {
          desiredLevel = "Level 1"; // TODO more Flexible
        }
        Boolean isImplementedForDesiredLevel = activity.isActivityImplemented().get(desiredLevel);
        if (isImplementedForDesiredLevel == null) {
          isImplementedForDesiredLevel = false;
        }
        flattenDate.addDynamicField(activity.getName(), isImplementedForDesiredLevel);
      }
      flattenDates.add(flattenDate);
    }
    return flattenDates;
  }

  @RequestMapping(value = "/matrix/overview/level/{level}", method = RequestMethod.GET)
  @ResponseBody
  public ArrayList<FlattenDate> getOverviewDashboardImplementedLevel(@PathVariable String level) throws Exception {
    ArrayList<FlattenDate> flattenDates = new ArrayList<>();
    for (Application application : applicationDirector.getApplications()) {
      FlattenDate flattenDate = new FlattenDate(new Date());
      flattenDate.addDynamicField("date", new Date());
      flattenDate.addDynamicField("Application", application.getTeam() + "-" + application.getName());
      for (Activity activity : application.getActivities()) {
        Boolean isImplemented = activity.isActivityImplemented().get(level);
        if (isImplemented == null) {
          isImplemented = false;
        }
        flattenDate.addDynamicField(activity.getName(), isImplemented);
      }
      flattenDates.add(flattenDate);
    }
    return flattenDates;
  }
}