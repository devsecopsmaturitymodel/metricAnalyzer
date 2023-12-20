package org.owasp.dsomm.metricca.analyzer.controller;

import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GrafanaTeamDashboardController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaTeamDashboardController.class);

  @Autowired
  private ApplicationDirector applicationDirector;

//  @Autowired
//  private TeamDashboard teamDashboard;


}