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

    }
}
