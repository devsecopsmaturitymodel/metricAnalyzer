package com.analyzer;

import java.util.Collection;
import java.util.Map;
import java.util.Iterator;

public class App {

    public static void main(String[] args) {
        // TODO: Scanner which gets all yaml files the config.yaml file --> put it in utils
        String yamlFilePath = "./activity_definitions/configuration.yaml";

        // TODO: utils
        Map<?, ?> configJavaYaml = YamlReader.convertYamlToJavaYaml(yamlFilePath);

        // Read App yaml TODO: Scanner
        String appPath = "./activity_definitions/App1.yaml";
        Map<?, ?> app1JavaYaml = YamlReader.convertYamlToJavaYaml(appPath);

        // Create App Class
        Application newApp = new Application(configJavaYaml);
        newApp.saveData(app1JavaYaml);

        // Example
        Collection activities = newApp.getActivities();
        Iterator iter = activities.iterator();
        while (iter.hasNext()) {
            Activity ac = (Activity) iter.next();
            Component cmp = (Component) (ac.getContent().get(0).get("read date"));
            System.out.println(cmp.getValue());
        }


    }
}
