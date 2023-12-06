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
public class YamlToObjectManager {
    private static final Logger logger = LoggerFactory.getLogger(YamlToObjectManager.class);

    private static Collection<Application> applications = new ArrayList<>();

    // Direct instantiation not possible
    private YamlToObjectManager() {    }

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
            Map<?, ?> app1JavaYaml = YamlReader.convertYamlToJavaYaml(yamlApplicationFilePath.getPath());
            assert app1JavaYaml != null;

            Application newApp = new Application(configJavaYaml);
            newApp.saveData(app1JavaYaml);
            newApp.setApplicationId((String) app1JavaYaml.get("applicationId"));
            newApp.setTeam((String) app1JavaYaml.get("team"));
            applications.add(newApp);
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
