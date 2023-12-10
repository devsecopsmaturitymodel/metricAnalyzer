package org.owasp.dsomm.metricca.analyzer.yaml.deserialization;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class YamlReader {

  // Input: Yaml file path (String)
  // Output: Map or null
  public static Map<?, ?> convertYamlToJavaYaml(String path) {
    if (fileExists(path)) {
      try {
        return convert(path);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("The YAML file does not exist.");
    }
    return null;
  }

  private static boolean fileExists(String filePath) {
    Path path = Paths.get(filePath);
    return Files.exists(path) && Files.isRegularFile(path);
  }

  private static Map<?, ?> convert(String path) throws FileNotFoundException {
    // Create an InputStream to read the file
    InputStream input = new FileInputStream(path);

    // Create a YAML parser
    Yaml yaml = new Yaml();

    // Parse the YAML file incrementally
    Iterable<Object> yamlObjects = yaml.loadAll(input);

    // Iterate over the YAML objects
    for (Object yamlObject : yamlObjects) {
      if (yamlObject instanceof Map<?, ?> documentMap) {
        // Handle each YAML document as a Map
        // Process the Map as needed
        return documentMap;
      }
    }
    // If no Map is found, throw an exception
    throw new IllegalStateException("No Map found in the YAML file.");
  }

}
