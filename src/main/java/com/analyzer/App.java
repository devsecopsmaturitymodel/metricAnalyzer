package com.analyzer;

import java.util.Map;

public class App {
    public static void main(String[] args) {
        String yamlFilePath = "./activity_definitions/configuration.yaml";

        Map<?, ?> yaml = YamlReader.convertYamlToJavaYaml(yamlFilePath);

        for (Map.Entry<?, ?> entry : yaml.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            System.out.println(key);
            System.out.println(value);
            System.out.println("----------------------");
        }

        // Initializes a new Activity Builder, creating a corresponding Activity along with an empty ArrayList for its components.
        ActivityBuilder builder = new ActivityBuilder();

        Activity activity = builder
            .setActivityName("conduction of simple threat modeling on a technical level")
            .setLevel("Level 2")
            .addStringComponent("title")
            .build();

        System.out.println(activity.getComponent().get(0).getValues());
    }
}
