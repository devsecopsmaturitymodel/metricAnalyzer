package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;

// Utilizes Singleton Design Pattern

public class YamlToObjectManager {
    private static final Logger logger = LoggerFactory.getLogger(YamlToObjectManager.class);

    private static Collection<Application> applications = new ArrayList<>();

    // Direct instantiation not possible
    private YamlToObjectManager() {    }

    public static Collection<Application> getApplications() {
//        if(activities == null || activities.isEmpty()) {
            initiateApplications();
//        }
        return applications;
    }

    //TODO Cronjob
    private static void initiateApplications() {
        // TODO: Scanner which gets all yaml files the configuration.yaml file --> put it in utils

        ConfigurationPath configurationPath = new ConfigurationPath();
        configurationPath.yamlConfigurationFilePath="/home/tpagel/git/metricAnalyzer/definitions/configuration.yaml";
        configurationPath.yamlApplicationFilePath="/home/tpagel/git/metricAnalyzer/definitions/App1.yaml";
        // TODO: utils
        logger.info("yamlConfigurationFilePath: " + configurationPath.yamlConfigurationFilePath);
        Map<?, ?> configJavaYaml = YamlReader.convertYamlToJavaYaml(configurationPath.yamlConfigurationFilePath);

        // Read App yaml TODO: Scanner
        logger.info("yamlApplicationFilePath: " + configurationPath.yamlApplicationFilePath);
        Map<?, ?> app1JavaYaml = YamlReader.convertYamlToJavaYaml(configurationPath.yamlApplicationFilePath);

        // Create App Class
        Application newApp = new Application(configJavaYaml);
        assert app1JavaYaml != null;
        newApp.saveData(app1JavaYaml);
        newApp.setApplicationId((String) app1JavaYaml.get("applicationId"));
        newApp.setTeam((String) app1JavaYaml.get("team"));

        applications.add(newApp);
    }
}
