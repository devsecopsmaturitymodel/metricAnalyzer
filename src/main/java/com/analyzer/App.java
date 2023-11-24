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
        System.out.println(activityDirector.getActivities().get("conduction of simple threat modeling on a technical level").getComponents());
        //System.out.println(activityDirector.getActivities().get("conduction of simple threat modeling on a technical level").getComponent().get(0).getName());

        Component test1 = (Component) activityDirector.getActivities().get("conduction of simple threat modeling on a technical level").getComponents().get("title");
        Component test2;
        try {
            test2 = ((Component) test1.clone());
            test1.setName("yamlFilePath");
            System.out.println(test1.getName());
            System.out.println(test2.getName());
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
