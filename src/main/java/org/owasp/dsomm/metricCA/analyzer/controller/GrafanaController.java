package org.owasp.dsomm.metricCA.analyzer.controller;

import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Activity;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Application;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.YamlToObjectManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class GrafanaController {
    private static final Logger logger = LoggerFactory.getLogger(GrafanaController.class);

    @RequestMapping(value = "/activities", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Activity> getActivities() {
        Collection<Activity> activities = new ArrayList<Activity>();
        for(Application application : YamlToObjectManager.getApplications()) {
            activities.addAll(application.getActivities());
        }
        return activities;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Application> getApplications() {
        return YamlToObjectManager.getApplications();
    }
//    @RequestMapping(value = "/team/{team}/application/{applicationId}/activity/{activity}", method = RequestMethod.GET)
//    @ResponseBody
//    public Collection<Activity> getTeamActivity(String teamName, String activityName) {
//        for(Application application :  YamlToObjectManager.getApplications()) {
//
//        }
//        return ;
//    }
}