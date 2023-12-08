package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.owasp.dsomm.metricCA.analyzer.exception.ComponentNotFoundException;
import org.owasp.dsomm.metricCA.analyzer.exception.SkeletonNotFoundException;
import org.owasp.dsomm.metricCA.analyzer.yamlDeserialization.components.DateComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.IOException;
import java.util.*;

// Utilizes Singleton Design Pattern
@Component
public class ApplicationDirector {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationDirector.class);

    private static Collection<Application> applications = new ArrayList<>();

    // Direct instantiation not possible
    private ApplicationDirector() {    }

    public Collection<Application> getApplications() throws SkeletonNotFoundException, ComponentNotFoundException, IOException, GitAPIException {
//        if(activities == null || activities.isEmpty()) {
        yamlScanner.enforceGitCloneIfTargetFolderExists = true; // set in cronjob
        initiateApplications();
        yamlScanner.enforceGitCloneIfTargetFolderExists = false;
//        }
        return applications;
    }
    @Autowired
    private YamlScanner yamlScanner;


    // TODO: CronJob
    private void initiateApplications() throws SkeletonNotFoundException, ComponentNotFoundException, IOException, GitAPIException {
        logger.info("yamlConfigurationFilePath: " + yamlScanner.getSkeletonYaml());
        Map<?, ?> configJavaYaml = YamlReader.convertYamlToJavaYaml(yamlScanner.getSkeletonYaml().getPath());
        ArrayList<Application> applications = new ArrayList<>();
        for(File yamlApplicationFilePath : yamlScanner.getApplicationYamls()) {
            logger.info("yamlApplicationFilePath: " + yamlApplicationFilePath.getPath());
            Map<?, ?> applicationYamlReader = YamlReader.convertYamlToJavaYaml(yamlApplicationFilePath.getPath());
            assert applicationYamlReader != null;
            switch((String) applicationYamlReader.get("kind")) { // TODO Something is wrong. "application" goes also into team
                case "application":
                case "team":
                    logger.debug("applicationYamlReader" + applicationYamlReader.toString());
                    Map<String, String> settingsApplicationYamlMap = (Map<String, String>) applicationYamlReader.get("settings");

                    // Create new Application
                    Application newApp = new Application(configJavaYaml); // Creates Skeleton
                    newApp.saveData(applicationYamlReader); // Assign the value from the application.yaml data to a component. 
                    newApp.setApplication((String) settingsApplicationYamlMap.get("application"));
                    newApp.setTeam((String) settingsApplicationYamlMap.get("team"));
                    newApp.setDesiredLevel((String) settingsApplicationYamlMap.get("desired level"));
                    applications.add(newApp);
                    break;
                default:
                    logger.error("Yaml file " + yamlApplicationFilePath.getPath() + " has no kind defined.");
                    break;
            }
        }
        this.applications = applications;
    }
    public Collection<Activity> getActivities(String activityName) {
        Collection<Activity> activitiesToReturn = new ArrayList<Activity>();
        for(Application application : applications) {
            for(Activity activity : application.getActivities()) {
                if(activity.getName().equals(activityName)) {
                    activitiesToReturn.add(activity);
                }
            }
        }
        return activitiesToReturn;
    }
    public Collection<Date> getDatesFromActivities(String activityName) {
        Collection<Date> datesToReturn = new ArrayList<Date>();
            for(Activity activity : this.getActivities(activityName)) {
                if (activity.getName().equals(activityName)) {
                    for(DateComponent date : activity.getDateComponents())
                    datesToReturn.add(date.getValue());
                }
            }
        return datesToReturn;
    }
}