package org.owasp.dsomm.metricca.analyzer.controller;

import org.owasp.dsomm.metricca.analyzer.grafana.OverviewDashboard;
import org.owasp.dsomm.metricca.analyzer.model.Activity;
import org.owasp.dsomm.metricca.analyzer.model.FlattenDate;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

@Controller
public class GrafanaTeamDashboardController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaTeamDashboardController.class);

  @Autowired
  private ApplicationDirector applicationDirector;

//  @Autowired
//  private TeamDashboard teamDashboard;


}