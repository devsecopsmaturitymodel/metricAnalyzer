package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


import java.util.Collection;
import java.util.Map;

// Utilizes Singleton Design Pattern

public class YamlToObjectManager {
    private static final Logger logger = LoggerFactory.getLogger(YamlToObjectManager.class);

    private static Collection<Activity> activities;

    // Direct instantiation not possible
    private YamlToObjectManager() {    }

    public static Collection<Activity> getActivities() {
        if(activities == null || activities.isEmpty()) {
            initiateActivities();
        }
        return activities;
    }

    //TODO Cronjob
    private static void initiateActivities() {
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

        // Example

        ObjectMapper mapper = new ObjectMapper();

        try {
            // convert user o,bject to json string and return it
            activities = newApp.getActivities();
            logger.info("Full objects:\n" + mapper.writeValueAsString(activities));
        }
        catch (JsonProcessingException e) {
            // catch various errors
            e.printStackTrace();
        }
    }
}
