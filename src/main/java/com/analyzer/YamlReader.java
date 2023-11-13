package com.analyzer;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlReader {

    public static void convertYamlToJavaObject(String path) {
        if (fileExists(path)) {
            convert(path);
        } else {
            System.out.println("The YAML file does not exist.");
        }

    }

    private static boolean fileExists(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isRegularFile(path);
    }

    private static void convert(String path) {
        try {

            // Create an InputStream to read the file
            InputStream input = new FileInputStream(path);

            // Create a YAML parser
            Yaml yaml = new Yaml();

            // Parse the YAML file incrementally
            Iterable<Object> yamlObjects = yaml.loadAll(input);

            // Iterate over the YAML objects
            for (Object yamlObject : yamlObjects) {
                if (yamlObject instanceof Map) {
                    // Handle each YAML document as a Map
                    Map<?, ?> documentMap = (Map<?, ?>) yamlObject;
                    // Process the Map as needed
                    System.out.println(documentMap);
                }
            }

        } catch (FileNotFoundException e) { 
            // should never be executed since it is checked before
            e.printStackTrace();
        }
    }
    
}
