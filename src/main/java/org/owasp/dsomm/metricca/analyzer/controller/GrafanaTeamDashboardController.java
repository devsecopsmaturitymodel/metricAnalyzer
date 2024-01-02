package org.owasp.dsomm.metricca.analyzer.controller;

import org.owasp.dsomm.metricca.analyzer.controller.dto.FlattenDate;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
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
import java.util.LinkedHashMap;

@Controller
public class GrafanaTeamDashboardController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaTeamDashboardController.class);

  @Autowired
  private ApplicationDirector applicationDirector;

  @RequestMapping(value = "/team/{teamName}/application/{application}/activity/{activityName}/simple", method = RequestMethod.GET)
  @ResponseBody
  public Collection<FlattenDate> getActivitiesPerTeamFlatSimple(@PathVariable String teamName, @PathVariable String application, @PathVariable String activityName) throws Exception {
    return applicationDirector.getActivitiesPerTeamAndApplicationFlat(application, teamName, activityName);
  }
  @RequestMapping(value = "/team/{teamName}/application/{application}/activity/{activityName}/map", method = RequestMethod.GET)
  @ResponseBody
  public LinkedHashMap<String, Collection<FlattenDate>> getActivitiesPerTeamFlatAsMap(@PathVariable String teamName, @PathVariable String application, @PathVariable String activityName) throws Exception {
    return applicationDirector.getActivitiesPerTeamAndApplicationFlatAsLevelMap(application, teamName, activityName);
  }
}