package com.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        String yamlFilePath = "./activity_definitions/configuration.yaml";

        // TODO: make own classes!

        // Check if the YAML file exists
        if (fileExists(yamlFilePath)) {
            executeCommand("cat", yamlFilePath);
        } else {
            System.out.println("The YAML file does not exist.");
        }
    }

    private static boolean fileExists(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isRegularFile(path);
    }

    private static void executeCommand(String... command) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);

        try {
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
