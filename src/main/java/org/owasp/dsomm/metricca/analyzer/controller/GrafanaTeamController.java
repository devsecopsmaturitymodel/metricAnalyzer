package org.owasp.dsomm.metricca.analyzer.controller;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.owasp.dsomm.metricca.analyzer.grafana.OverviewDashboard;
import org.owasp.dsomm.metricca.analyzer.grafana.PanelConfiguration;
import org.owasp.dsomm.metricca.analyzer.model.threshold.Thresholds;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.ThresholdParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Controller
public class GrafanaTeamController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaTeamController.class);

  @Autowired
  private ApplicationDirector applicationDirector;

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