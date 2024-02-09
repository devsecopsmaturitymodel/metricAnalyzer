package org.owasp.dsomm.metricca.analyzer.controller;

import org.owasp.dsomm.metricca.analyzer.controller.dto.FlattenDate;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.SecurityTrainingActivity;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.component.DatePeriodHoursAndPeople;
import org.owasp.dsomm.metricca.analyzer.grafana.GrafanaDashboardCreator;
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
public class GrafanaMatrixDashboardController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaMatrixDashboardController.class);

  @Autowired ApplicationDirector applicationDirector;

  @RequestMapping(value = "/matrix/overview", method = RequestMethod.GET)
  @ResponseBody
  public ArrayList<FlattenDate> getOverviewDashboard() throws Exception {
    ArrayList<FlattenDate> flattenDates = new ArrayList<>();
    for(Application application : applicationDirector.getApplications()) {
      FlattenDate flattenDate = new FlattenDate(new Date());
      flattenDate.addDynamicField("date", new Date());
      flattenDate.addDynamicField("team", application.getTeam() + " " + application.getDesiredLevel());
      for(Activity activity : application.getActivities()) {
        String desiredLevel = application.getDesiredLevel();
        if(application.getDesiredLevel() == null) {
          desiredLevel = "Level 1"; // TODO more Flexible
        }
        Boolean isImplemented = activity.isActivityImplemented().get(desiredLevel);
        if(isImplemented == null) {
          isImplemented = false;
        }
        flattenDate.addDynamicField(activity.getName(), isImplemented);
      }
      flattenDates.add(flattenDate);
    }
    return flattenDates;
  }
}