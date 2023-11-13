package com.analyzer;

public class App {
    public static void main(String[] args) {
        String yamlFilePath = "./activity_definitions/configuration.yaml";

        YamlReader.convertYamlToJavaObject(yamlFilePath);
    }
}
