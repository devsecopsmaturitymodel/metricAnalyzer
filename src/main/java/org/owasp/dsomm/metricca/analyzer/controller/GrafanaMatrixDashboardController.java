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

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class GrafanaMatrixDashboardController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaMatrixDashboardController.class);

  @Autowired
  private GrafanaDashboardCreator grafanaDashboardCreator;



  @RequestMapping(value = "/matrix/overview", method = RequestMethod.GET)
  @ResponseBody
  public String getOverviewDashboard() throws Exception {
    return grafanaDashboardCreator.getDashboards().get("matrix");
  }
}