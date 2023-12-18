package org.owasp.dsomm.metricCA.analyzer.controller;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.owasp.dsomm.metricCA.analyzer.model.FlattenDate;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Activity;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.Application;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.YamlToObjectManager;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.components.DateComponent;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.components.DatePeriodComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
public class GrafanaController {
    private static final Logger logger = LoggerFactory.getLogger(GrafanaController.class);

    @Autowired
    private YamlToObjectManager yamlToObjectManager;

    @RequestMapping(value = "/activities", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Activity> getActivities() throws Exception {
        Collection<Activity> activities = new ArrayList<Activity>();
        for (Application application : yamlToObjectManager.getApplications()) {
            activities.addAll(application.getActivities());
        }
        return activities;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Application> getApplications() throws Exception {
        return yamlToObjectManager.getApplications();
    }

    @RequestMapping(value = "/team/{teamName}/applications", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Application> getTeamApplications(@PathVariable String teamName) throws Exception {
        Collection<Application> applicationsToReturn = new ArrayList<Application>();
        for (Application application : yamlToObjectManager.getApplications()) {
            if (application.getTeam().equals(teamName)) {
                applicationsToReturn.add(application);
            }
        }
        return applicationsToReturn;
    }

    @RequestMapping(value = "/team/{teamName}/applicationIds", method = RequestMethod.GET)
    @ResponseBody
    public Collection<String> getTeamApplicationIds(@PathVariable String teamName) throws Exception {
        Collection<String> applicationsToReturn = new ArrayList<String>();
        for (Application application : yamlToObjectManager.getApplications()) {
            if (application.getTeam().equals(teamName)) {
                applicationsToReturn.add(application.getApplicationId());
            }
        }
        applicationsToReturn.add("all");
        return applicationsToReturn;
    }

    @RequestMapping(value = "/activity/{activityName}", method = RequestMethod.GET)
    @ResponseBody
    public Collection<FlattenDate> getActivitiesFlat(@PathVariable String activityName) throws Exception {
        Collection<FlattenDate> flattendActivitiesToReturn = new ArrayList<FlattenDate>();
        Collection<Date> datesFromActivities = yamlToObjectManager.getDatesFromActivities(activityName);
        logger.info("dates: " + datesFromActivities);
        for (Date date : datesFromActivities) {
            FlattenDate flattenDate = new FlattenDate(date);
            for (Application application : yamlToObjectManager.getApplications()) {
                for (Activity activity : application.getActivities(activityName)) {
                        boolean value = false;
                        logger.debug("Found activity: " + activity.getName() + " in application: " + application.getApplicationId());
                        DateComponent dateComponent = getActivityMatchingDate(activity, date);
                        logger.info("dateComponent: " + dateComponent);
                        if (dateComponent != null) {
                                logger.info("date == dateComponent.getValue()" + dateComponent.getValue());
                                if (dateComponent instanceof DatePeriodComponent) {
                                    value = activity.getDatePeriodOrEndComponent().isActive();
                                } else {
                                    value = true;
                                }
                        }
                        flattenDate.addDynamicField(application.getTeam() + "-" + application.getApplicationId(), value);
                    }

            }
            flattendActivitiesToReturn.add(flattenDate);
        }
        logger.debug("activitiesToReturn: " + flattendActivitiesToReturn);
        return flattendActivitiesToReturn;
    }

    private DateComponent getActivityMatchingDate(Activity activity, Date givenDate) {
        for (DateComponent dateComponent : activity.getDateComponents()) {
            if (dateComponent.getValue().equals(givenDate)) {
                return dateComponent;
            }
        }
        return null;
    }

    @RequestMapping(value = "/team/{teamName}/application/{applicationId}/activity/{activityName}", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Activity> getTeamActivity(@PathVariable String teamName, @PathVariable String
            applicationId, @PathVariable String activityName) throws Exception {
        Collection<Activity> activitiesToReturn = new ArrayList<Activity>();
        logger.info("in teamGetActivity");
        for (Application application : yamlToObjectManager.getApplications()) {
            if (application.getTeam().equals(teamName) || teamName.equals("all")) {
                if (application.getApplicationId().equals(applicationId) || applicationId.equals("all")) {
                    for (Activity activity : application.getActivities()) {
                        if (activity.getName().equals(activityName)) {
                            logger.debug("Found activity: " + activity.getName() + " in application: " + application.getApplicationId());

                            activitiesToReturn.add(activity);
                        }
                    }
                }
            }
        }
        logger.debug("activitiesToReturn: " + activitiesToReturn);
        return activitiesToReturn;
    }

    @RequestMapping(value = "/applications", method = RequestMethod.GET)
    @ResponseBody
    public Collection<String> getApplicationIds() throws IOException, GitAPIException {
        Set<String> applicationIds = new HashSet<>();
        for (Application application : yamlToObjectManager.getApplications()) {
            applicationIds.add(application.getApplicationId());
        }
        return applicationIds;
    }

    @RequestMapping(value = "/teams", method = RequestMethod.GET)
    @ResponseBody
    public Collection<String> getTeams() throws IOException, GitAPIException {
        Set<String> teams = new HashSet<>();
        for (Application application : yamlToObjectManager.getApplications()) {
            teams.add(application.getTeam());
        }
        teams.add("all");
        return teams;
    }
}