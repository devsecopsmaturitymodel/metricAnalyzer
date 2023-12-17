package org.owasp.dsomm.metricca.analyzer.controller;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.yaml.deserialization.ApplicationDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Controller
public class GrafanaApplicationController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaApplicationController.class);

  @Autowired
  private ApplicationDirector applicationDirector;

  @RequestMapping(value = "/applications", method = RequestMethod.GET)
  @ResponseBody
  public Collection<String> getApplicationIds() throws IOException, GitAPIException {
    Set<String> applicationIds = new HashSet<>();
    for (Application application : applicationDirector.getApplications()) {
      applicationIds.add(application.getApplication());
    }
    return applicationIds;
  }

}