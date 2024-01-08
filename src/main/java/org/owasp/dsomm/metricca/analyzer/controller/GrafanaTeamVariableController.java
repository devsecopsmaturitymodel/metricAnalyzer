package org.owasp.dsomm.metricca.analyzer.controller;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Controller
public class GrafanaTeamVariableController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaTeamVariableController.class);

  @Autowired
  private ApplicationDirector applicationDirector;

  @RequestMapping(value = "/teams", method = RequestMethod.GET)
  @ResponseBody
  public Collection<String> getTeams() throws IOException, GitAPIException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    Set<String> teams = new HashSet<>();
    for (Application application : applicationDirector.getApplications()) {
      teams.add(application.getTeam());
    }
    return teams;
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
}