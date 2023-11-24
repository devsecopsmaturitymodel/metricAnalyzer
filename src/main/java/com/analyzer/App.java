package com.analyzer;

import java.util.Map;

public class App {
    public static void main(String[] args) {
        // TODO: Scanner which gets all yaml files the config.yaml file --> put it in utils
        String yamlFilePath = "./activity_definitions/configuration.yaml";

        // TODO: utils
        Map<?, ?> javaYaml = YamlReader.convertYamlToJavaYaml(yamlFilePath);

        // Create all Activities
        ActivityDirector activityDirector = new ActivityDirector();
        activityDirector.createActivities(javaYaml);

        // Example
        //System.out.println(activityDirector.getActivities().get("conduction of simple threat modeling on a technical level").getComponents());
        //System.out.println(activityDirector.getActivities().get("conduction of simple threat modeling on a technical level").getComponent().get(0).getName());

        activityDirector.getActivities().get("conduction of simple threat modeling on a technical level").addContent();
        activityDirector.getActivities().get("conduction of simple threat modeling on a technical level").addContent();
        System.out.println(activityDirector.getActivities().get("conduction of simple threat modeling on a technical level").getLevel());

    }
}
