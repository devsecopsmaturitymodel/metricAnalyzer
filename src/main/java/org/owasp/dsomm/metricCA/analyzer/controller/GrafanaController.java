package org.owasp.dsomm.metricCA.analyzer.controller;

import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Activity;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Application;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.YamlToObjectManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.*;

@Controller
public class GrafanaController {
    private static final Logger logger = LoggerFactory.getLogger(GrafanaController.class);

    @Autowired
    private YamlToObjectManager yamlToObjectManager;
    @RequestMapping(value = "/activities", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Activity> getActivities() throws FileNotFoundException {
        Collection<Activity> activities = new ArrayList<Activity>();
        for(Application application : yamlToObjectManager.getApplications()) {
            activities.addAll(application.getActivities());
        }
        return activities;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Application> getApplications() throws FileNotFoundException {
        return yamlToObjectManager.getApplications();
    }

    @RequestMapping(value = "/team/{team}/application", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Application> getTeamApplications(String teamName) throws FileNotFoundException {
        Collection<Application> applicationsToReturn = new ArrayList<Application>();
        for(Application application :  yamlToObjectManager.getApplications()) {
            if(application.getTeam().equals(teamName)) {
                applicationsToReturn.add(application);
            }
        }
        return applicationsToReturn;
    }
    @RequestMapping(value = "/team/{team}/application/{applicationId}/activity/{activity}", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Activity> getTeamActivity(String teamName, String activityName) throws FileNotFoundException {
        Collection<Activity> activitiesToReturn = new ArrayList<Activity>();
        for(Application application :  yamlToObjectManager.getApplications()) {
            if(application.getTeam().equals(teamName) || teamName.equals("all")) {
                for(Activity activity : application.getActivities()) {
                    if(activity.getName().equals(activityName)) {
                        activitiesToReturn.add(activity);
                    }
                }
            }
        }
        return activitiesToReturn;
    }
    public Collection<String> getApplicationIds() throws FileNotFoundException {
        Set<String> applicationIds = new HashSet<>();
        for (Application application : yamlToObjectManager.getApplications()) {
            applicationIds.add(application.getApplicationId());
        }
        return applicationIds;
    }

    @RequestMapping(value = "/teams", method = RequestMethod.GET)
    @ResponseBody
    public Collection<String> getTeams() throws FileNotFoundException {
        Set<String> teams = new HashSet<>();
        for (Application application : yamlToObjectManager.getApplications()) {
            teams.add(application.getTeam());
        }
        return teams;
    }
}