package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

// Utilizes Singleton Design Pattern
@Component
public class YamlToObjectManager {
    private static final Logger logger = LoggerFactory.getLogger(YamlToObjectManager.class);

    private static Collection<Application> applications = new ArrayList<>();

    // Direct instantiation not possible
    private YamlToObjectManager() {    }

    public Collection<Application> getApplications() throws FileNotFoundException {
//        if(activities == null || activities.isEmpty()) {
            initiateApplications();
//        }
        return applications;
    }
    @Autowired
    private YamlScanner yamlScanner;

    // TODO: CronJob
    private void initiateApplications() throws FileNotFoundException {
        // TODO: Scanner which gets all yaml files the configuration.yaml file --> put it in utils

//        YamlScanner yamlScanner = new YamlScanner();
        logger.info("yamlConfigurationFilePath: " + yamlScanner.getSkeletonYaml());
        Map<?, ?> configJavaYaml = YamlReader.convertYamlToJavaYaml(yamlScanner.getSkeletonYaml().getPath());

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
    }
}
